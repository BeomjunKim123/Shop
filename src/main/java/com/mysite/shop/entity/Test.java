package com.mysite.shop.entity;

import jakarta.persistence.*;


import com.mysite.shop.constant.Role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter @Setter
@ToString
public class Test {
    @Id
    @Column(name="Test_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;
}
