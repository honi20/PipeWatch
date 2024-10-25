package com.pipewatch.user.model.entity;

public enum Role {
    ROLE_EMPLOYEE("사원"),
    ROLE_ADMIN("관리자"),
    ROLE_ENTERPRISE("기업");

    String value;

    Role(String value) {
        this.value = value;
    }
}
