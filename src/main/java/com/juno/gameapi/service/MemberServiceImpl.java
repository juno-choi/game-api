package com.juno.gameapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juno.gameapi.service.dto.JwtToken;
import com.juno.gameapi.controller.dto.RequestLogin;
import com.juno.gameapi.controller.dto.RequestMember;
import com.juno.gameapi.webclient_config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final WebClientConfig webClient;

    @Override
    public ResponseEntity<String> join(RequestMember requestMember) {
        ResponseEntity<String> result = webClient.webClient().post()  //get 요청
                .uri("/login-service/game/join")    //요청 uri
                .body(Mono.just(requestMember), RequestMember.class)
                .retrieve()//결과 값 반환
                .toEntity(String.class)
                .block();
        return result;
    }

    @Override
    public ResponseEntity<String> login(RequestLogin requestLogin, HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<String> result = webClient.webClient().post()  //get 요청
                .uri("/login-service/game/login")    //요청 uri
                .body(Mono.just(requestLogin), RequestLogin.class)
                .retrieve()//결과 값 반환
                .toEntity(String.class)
                .block();

        //token parse
        String body = result.getBody();
        try {
            JwtToken token = new ObjectMapper().readValue(body, JwtToken.class);
            String accessToken = token.getAccess_token();
            String refreshToken = token.getRefresh_token();
            String userId = requestLogin.getUserId();

            log.debug("accessToken = {}", accessToken);
            log.debug("refreshToken = {}", refreshToken);

            //request에서 session 전달 받기
            String accessTokenId = UUID.randomUUID().toString();
            String refreshTokenId = UUID.randomUUID().toString();
            String userNameId = UUID.randomUUID().toString();
            HttpSession session = request.getSession(true); //true를 주면 session이 없을 때 session을 만들고 시작함
            session.setAttribute(accessTokenId, accessToken);   //session에 access_token 값을 저장
            session.setAttribute(refreshTokenId, refreshToken);   //session에 refresh_token 값을 저장 , remeber me 의 기능을 구현하고 싶다면 db에 저장
            session.setAttribute(userNameId, userId);   //session에 refresh_token 값을 저장 , remeber me 의 기능을 구현하고 싶다면 db에 저장

            //cookie에는 session id 값을 저장
            Cookie accessTokenCookie = new Cookie("access_token_id_cookie", accessTokenId);
            accessTokenCookie.setMaxAge(30*60);    //단위는 초 , 30분으로 지정
            accessTokenCookie.setPath("/");
            accessTokenCookie.setHttpOnly(true);    //http를 통해서만 쿠키가 보내짐
            //accessTokenCookie.setSecure(true);    //https에서만 쿠키가 보내지도록
            Cookie refreshTokenCookie = new Cookie("refresh_token_id_cookie", refreshTokenId);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setMaxAge(7*24*60*60);    //7일
            refreshTokenCookie.setPath("/");
            Cookie memberCookie = new Cookie("user_id", userNameId);
            memberCookie.setHttpOnly(true);
            memberCookie.setPath("/");

            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
            response.addCookie(memberCookie);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        return result;
    }
}
