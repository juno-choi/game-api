package com.juno.gameapi.service;

import com.juno.gameapi.controller.dto.RequestLogin;
import com.juno.gameapi.controller.dto.RequestMember;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

public interface MemberService {
    ResponseEntity<String> join(RequestMember requestMember);

    ResponseEntity<String> login(RequestLogin requestLogin, HttpServletResponse response);
}
