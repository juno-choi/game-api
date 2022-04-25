package com.juno.gameapi.api;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonApi<T> {
    private String code;
    private String msg;
    private T data;

    @Builder
    public CommonApi(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
