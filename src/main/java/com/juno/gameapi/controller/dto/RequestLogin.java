package com.juno.gameapi.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestLogin {
    private String userId;
    private String pw;
}
