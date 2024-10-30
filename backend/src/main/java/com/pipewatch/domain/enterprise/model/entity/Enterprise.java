package com.pipewatch.domain.enterprise.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Enterprise {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enterprise_id")
    private Long id;

    private String name;
    private String industry;

    @Column(name = "manager_email")
    @Email(message = "유효한 이메일 형식을 입력해 주세요")
    private String managerEmail;

    @Column(name = "manager_phone_number")
    private String managerPhoneNumber;

    @Column(name = "is_active")
    private Boolean isActive;
}
