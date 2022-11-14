package com.starters.applyservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@ToString
public class Lesson {

    @Id
    @GeneratedValue
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "application_id")
//    private Application application;

    private String className;

    private LocalDateTime classStart;

    private LocalDateTime classEnd;

    private LocalDateTime recruitmentStart;

    private LocalDateTime recruitmentEnd;

    private String status;
}
