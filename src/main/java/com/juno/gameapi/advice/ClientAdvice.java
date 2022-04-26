package com.juno.gameapi.advice;

import com.juno.gameapi.exception.api.ClientError;
import com.juno.gameapi.exception.api.ServerError;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClientAdvice {

    //4xx 에러
    @ExceptionHandler(ClientError.class)
    public ResponseEntity<String> clientError(ClientError ce){
        return ResponseEntity.status(ce.getStatus()).contentType(MediaType.APPLICATION_JSON).body(ce.getBody());
    }

    //4xx 에러
    @ExceptionHandler(ServerError.class)
    public ResponseEntity<String> serverError(ServerError se){
        return ResponseEntity.status(se.getStatus()).contentType(MediaType.APPLICATION_JSON).body(se.getBody());
    }
}
