package com.pipewatch.domain.user.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponse {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MyPageDto {
		private String name;
		private String email;
		private String enterpriseName;
		private Long empNo;
		private String department;
		private String empClass;
		private String roll;
		private String state;
	}
}
