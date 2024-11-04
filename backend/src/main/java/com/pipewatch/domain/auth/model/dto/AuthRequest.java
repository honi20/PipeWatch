package com.pipewatch.domain.auth.model.dto;

import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.State;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.global.jwt.entity.JwtToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequest {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EmailCodeSendDto {
		@Schema(description = "인증코드 전송할 이메일", example = "paori@ssafy.com")
		private String email;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EmailCodeVerifyDto {
		@Schema(description = "인증코드가 전송된 이메일", example = "paori@ssafy.com")
		private String email;
		@Schema(description = "인증코드", example = "603942")
		private String verifyCode;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SignupDto {
		@Schema(description = "이메일", example = "paori@ssafy.com")
		private String email;
		@Schema(description = "비밀번호", example = "ssafy1234")
		private String password;
		@Schema(description = "이름", example = "파오리")
		private String name;
		@Schema(description = "기업 ID", example = "1")
		private Long enterpriseId;
		@Schema(description = "사번", example = "1123456")
		private Long empNo;
		@Schema(description = "부서", example = "IT 개발부")
		private String department;
		@Schema(description = "직급", example = "대리")
		private String empClass;
		@Schema(description = "메일 인증코드", example = "603942")
		private String verifyCode;

		public User toEntity(String uuid) {
			return User.builder()
					.email(this.email)
					.password(this.password)
					.name(this.name)
					.state(State.PENDING)
					.role(Role.USER)
					.uuid(uuid)
					.build();
		}

		public JwtToken toRedis(String uuid, Long userId, String refreshToken) {
			return JwtToken.builder()
					.uuid(uuid)
					.userId(userId)
					.refreshToken(refreshToken)
					.build();
		}
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EnterpriseRegistDto {
		private String name;
		private String industry;
		private String managerEmail;
		private String managerPhoneNumber;

		public Enterprise toEntity(User user) {
			return Enterprise.builder()
					.name(this.name)
					.industry(this.industry)
					.managerEmail(this.managerEmail)
					.managerPhoneNumber(this.managerPhoneNumber)
					.isActive(true)
					.user(user)
					.build();
		}
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SigninDto {
		private String email;
		private String password;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EmailPwdSendDto {
		private String email;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PasswordResetDto {
		private String pwdUuid;
		private String newPassword;
	}
}
