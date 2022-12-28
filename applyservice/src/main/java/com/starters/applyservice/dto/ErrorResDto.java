package com.starters.applyservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResDto {
    private final int code;
    private final String message;
}
