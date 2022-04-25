package com.juno.gameapi.service;

import com.juno.gameapi.controller.dto.RequestMember;
import com.juno.gameapi.webclient_config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
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
}
