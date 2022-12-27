package com.starters.applyservice.dto;

import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class ResponseDto<T> {
    private final int status;
    private final String message;
    private final T data;

    public ResponseDto(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
