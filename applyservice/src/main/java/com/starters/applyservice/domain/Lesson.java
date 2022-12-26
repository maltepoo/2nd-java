package com.starters.applyservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@ToString
public class Lesson {

    @Id
    @GeneratedValue
    private Long id;

    private String className;

    private LocalDate classStart;

    private LocalDate classEnd;

    private LocalDate recruitmentStart;

    private LocalDate recruitmentEnd;

    private Boolean status;
    // true 모집중, false 모집완료
}
