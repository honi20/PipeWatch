package com.pipewatch.domain.user.model.entity;

import java.util.Arrays;

public enum Role {
	ROLE_USER("일반회원"),
	ROLE_EMPLOYEE("사원"),
	ROLE_ADMIN("관리자"),
	ROLE_ENTERPRISE("기업");

	String value;

	Role(String value) {
		this.value = value;
	}

	public static boolean isValidRole(String role) {
		return Arrays.stream(Role.values())
				.anyMatch(r -> r.name().equals(role));
	}
}
