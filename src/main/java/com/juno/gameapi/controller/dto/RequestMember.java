package com.juno.gameapi.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestMember {
    private String userId;
    private String pw;
    private String rePw;
    private String name;
}
