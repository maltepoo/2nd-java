package com.starters.applyservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter @Setter
@ToString
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name="application_id")
//    private Application application;

    private String email;

    private String password;

    private String name;

    private Boolean isAdmin;

    private String contacts;
}
