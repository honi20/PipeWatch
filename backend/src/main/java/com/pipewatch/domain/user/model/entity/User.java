package com.pipewatch.domain.user.model.entity;

import com.pipewatch.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Email
    @NotNull
    @Column(unique=true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    @Builder.Default
    private State state = State.PENDING;

    @NotNull
    @Builder.Default
    private Role role = Role.ROLE_EMPLOYEE;

    @NotNull
    private String uuid;

    public void updateState(State state) {
        this.state = state;
    }
}
