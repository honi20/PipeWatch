package com.pipewatch.domain.enterprise.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Enterprise {

    @Id @GeneratedValue
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
