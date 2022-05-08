package com.juno.gameapi.controller;

import com.juno.gameapi.controller.dto.RequestLogin;
import com.juno.gameapi.controller.dto.RequestMember;
import com.juno.gameapi.service.MemberService;
import com.juno.gameapi.service.dto.ResponseLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/member")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody RequestMember requestMember){
        log.info("회원가입 요청 : {}", requestMember.getUserId());
        ResponseEntity<String> responseEntity = memberService.join(requestMember);
        return ResponseEntity.status(responseEntity.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(responseEntity.getBody());
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseLogin> login(@RequestBody RequestLogin requestLogin, HttpServletRequest request, HttpServletResponse response){
        ResponseEntity<ResponseLogin> responseEntity = memberService.login(requestLogin, request, response);
        return ResponseEntity.status(responseEntity.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(responseEntity.getBody());
    }

    //token check
    @GetMapping("/tokenCheck")
    public ResponseEntity<String> tokenCheck(){
        return ResponseEntity.ok().build();
    }
}
