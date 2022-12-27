package com.starters.applyservice.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestApplicationDto {
    private String applyMotiv;
    private String futureCareer;

    public RequestApplicationDto(String applyMotiv, String futureCareer) {
        this.applyMotiv = applyMotiv;
        this.futureCareer = futureCareer;
    }
}
