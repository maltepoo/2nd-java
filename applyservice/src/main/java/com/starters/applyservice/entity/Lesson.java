package com.starters.applyservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

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
