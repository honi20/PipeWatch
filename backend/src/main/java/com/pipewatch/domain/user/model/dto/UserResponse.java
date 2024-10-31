package com.pipewatch.domain.user.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class UserResponse {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	public static class MyPageDto {
		private String name;
		private String email;
		private String role;
		private String state;
		private String enterpriseName;
		private EmployeeDto employee;

		// Projections.constructor에서 필요한 생성자 추가
		public MyPageDto(String name, String email, String role, String state, String enterpriseName, EmployeeDto employee) {
			this.name = name;
			this.email = email;
			this.role = role;
			this.state = state;
			this.enterpriseName = enterpriseName;
			this.employee = employee;
		}
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EmployeeDto {
		private Long empNo;
		private String department;
		private String empClass;
	}
}
