package com.juno.gameapi.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juno.gameapi.exception.api.ClientError;
import com.juno.gameapi.service.dto.JwtToken;
import com.juno.gameapi.webclient_config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    private final WebClientConfig webClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("Token Interceptor 실행");
        log.info("요청 url = {}", request.getRequestURL());

        //session 가져오기
        HttpSession session = request.getSession(false);

        //cookie 가져오기
        Cookie[] cookies = request.getCookies();

        String accessTokenId = "";
        String refreshTokenId = "";
        String accessToken = "";
        String refreshToken = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info("access token 확인");
            accessTokenId = findCookie(cookies, "access_token_id_cookie").getValue();
            accessToken = session.getAttribute(accessTokenId).toString();
            log.info("access token = {}", accessToken);
        }catch (NullPointerException ne){
            try{
                log.info("access token 만료로 인한 refresh token 확인");
                refreshTokenId = findCookie(cookies, "refresh_token_id_cookie").getValue();
                refreshToken = session.getAttribute(refreshTokenId).toString();
                log.info("refresh token = {}", refreshToken);

                //refresh token 존재한다면 다시 access token 발급
                ResponseEntity<String> result = webClient.webClient().post()  //get 요청
                        .uri("/login-service/game/token/refresh")    //요청 uri
                        .header("Authorization", String.format("Bearer %s", refreshToken))
                        .retrieve()//결과 값 반환
                        .toEntity(String.class)
                        .block();

                //token 파싱
                String body = result.getBody();
                JwtToken jwtToken = mapper.readValue(body, JwtToken.class);
                accessToken = jwtToken.getAccess_token();

                accessTokenId = UUID.randomUUID().toString();
                session.setAttribute(accessTokenId, accessToken);   //session에 access_token 값을 저장

                //쿠키도 새로 생성
                Cookie accessTokenCookie = new Cookie("access_token_id_cookie", accessTokenId);
                accessTokenCookie.setMaxAge(30*60);    //단위는 초 , 30분으로 지정
                accessTokenCookie.setPath("/");
                accessTokenCookie.setHttpOnly(true);    //http를 통해서만 쿠키가 보내짐
                response.addCookie(accessTokenCookie);

            }catch (NullPointerException ne2){
                log.info("token 만료");
                //token 없음으로 재 로그인 필요
                Map<String, Object> map = new HashMap<>();
                map.put("code", 401);
                map.put("msg", "로그인 후 다시 시도해주세요.");
                throw new ClientError(HttpStatus.UNAUTHORIZED, mapper.writeValueAsString(map));
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Token Interceptor 종료");
    }
    
    //cookie 찾기
    private Cookie findCookie(Cookie[] cookies, String findKey){
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(findKey))
                .findAny()
                .orElse(null);
    }
}