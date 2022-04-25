package com.juno.gameapi.controller;

import com.juno.gameapi.controller.dto.RequestMember;
import com.juno.gameapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody RequestMember requestMember){
        ResponseEntity<String> responseEntity = memberService.join(requestMember);
        return ResponseEntity.status(responseEntity.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(responseEntity.getBody());
    }
}
