package com.starters.applyservice.dto;

import com.starters.applyservice.entity.ApplyStatus;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ApplicationDto {
    private Long appId;
    private Long memberId;
    private Long lessonId;
    private String applyMotiv;
    private String futureCareer;
    private ApplyStatus status;

    public ApplicationDto(Long appId, Long memberId, Long lessonId, String applyMotiv, String futureCareer, ApplyStatus status) {
        this.appId = appId;
        this.memberId = memberId;
        this.lessonId = lessonId;
        this.applyMotiv = applyMotiv;
        this.futureCareer = futureCareer;
        this.status = status;
    }
}
