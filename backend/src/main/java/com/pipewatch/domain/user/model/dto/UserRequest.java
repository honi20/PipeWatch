package com.pipewatch.domain.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRequest {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MyPageModifyDto {
		@Schema(description = "변경된 부서", example = "경영지원부")
		private String department;
		@Schema(description = "변경된 직급", example = "팀장")
		private String empClass;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PasswordModifyDto {
		@Schema(description = "새 비밀번호", example = "newPassword")
		private String newPassword;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class WithdrawDto {
		@Schema(description = "비밀번호", example = "password")
		private String password;
	}
}
