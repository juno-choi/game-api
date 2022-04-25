package com.juno.gameapi.service;

import com.juno.gameapi.controller.dto.RequestMember;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    ResponseEntity<String> join(RequestMember requestMember);
}
