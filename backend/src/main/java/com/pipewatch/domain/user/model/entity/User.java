package com.pipewatch.domain.user.model.entity;

import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import com.pipewatch.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Email
    private String email;

    private String password;

    private String name;

    private State state;

    private Role role;

    private String uuid;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;
}
