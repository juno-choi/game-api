package com.juno.gameapi.exception.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

//400번대 에러 처리
@Getter
public class ClientError extends RuntimeException{
    private HttpStatus status;
    private String body;

    public ClientError(HttpStatus status, String body) {
        this.status = status;
        this.body = body;
    }
}
