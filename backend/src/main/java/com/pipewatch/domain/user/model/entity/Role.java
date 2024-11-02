package com.pipewatch.domain.user.model.entity;

import java.util.Arrays;

public enum Role {
	ROLE_USER,
	ROLE_EMPLOYEE,
	ROLE_ADMIN,
	ROLE_ENTERPRISE;

	public static boolean isValidRole(String role) {
		return Arrays.stream(Role.values())
				.anyMatch(r -> r.name().equals(role));
	}
}
