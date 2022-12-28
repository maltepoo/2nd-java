package com.starters.applyservice.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;

@Data
@ResponseBody
public class ResponseDto<T> {
    private final int status;
    private final String message;
    private final T data;

    public ResponseDto(HttpStatus status, String message, T data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

}
