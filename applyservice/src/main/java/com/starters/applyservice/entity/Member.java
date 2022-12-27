package com.starters.applyservice.entity;

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
    private Long id;

    private String email;

    private String password;

    private String name;

    private Boolean isAdmin;

    private String contacts;
}
