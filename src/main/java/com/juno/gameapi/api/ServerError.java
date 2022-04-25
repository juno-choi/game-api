package com.juno.gameapi.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

//5xx 에러
@Getter
public class ServerError extends RuntimeException{
    private HttpStatus status;
    private String body;

    public ServerError(HttpStatus status, String body) {
        this.status = status;
        this.body = body;
    }
}
