package com.pipewatch.domain.auth.model.dto;

import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.State;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.global.jwt.entity.JwtToken;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailCodeSendRequestDto {
        private String email;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailCodeVerifyRequestDto {
        private String email;
        private String verifyCode;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupRequestDto {
        private String email;
        private String password;
        private String name;
        private Long enterpriseId;
        private Long empNo;
        private String department;
        private String empClass;
        private String verifyCode;

        public User toEntity(String uuid) {
            return User.builder()
                    .email(this.email)
                    .password(this.password)
                    .name(this.name)
                    .state(State.PENDING)
                    .role(Role.ROLE_EMPLOYEE)
                    .uuid(uuid)
                    .build();
        }

        public JwtToken toRedis(String uuid, Long userId, String refreshToken){
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
    public static class AccessTokenResponseDto {
        private String accessToken;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnterpriseRegistRequestDto {
        private String name;
        private String industry;
        private String managerEmail;
        private String managerPhoneNumber;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SigninRequestDto {
        private String email;
        private String password;
    }
}
