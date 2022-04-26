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

import javax.servlet.http.HttpServletResponse;

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
    public ResponseEntity<String> login(RequestLogin requestLogin, HttpServletResponse response) {
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

            log.debug("accessToken = {}", accessToken);
            log.debug("refreshToken = {}", refreshToken);

            // access = header 전달
            // refresh = db 저장
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        return result;
    }
}
