package com.juno.gameapi.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtToken {
    private String access_token;
    private String refresh_token;
    private ResponseLogin user;
}
