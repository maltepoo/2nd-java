package com.starters.applyservice.entity;

public enum ApplyStatus {
    APPLYING("지원중"),
    SUBMITTED("제출완료"),
    ACCEPTED("합격"),
    REJECTED("불합격");

    private final String customType;

    ApplyStatus (String customType){
        this.customType = customType;
    }
}