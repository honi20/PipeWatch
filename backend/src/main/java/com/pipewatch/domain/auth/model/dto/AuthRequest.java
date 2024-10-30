package com.pipewatch.domain.auth.model.dto;

import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.State;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.global.jwt.entity.JwtToken;
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
        private String email;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailCodeVerifyDto {
        private String email;
        private String verifyCode;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupDto {
        private String email;
        private String password;
        private String name;
        private Long enterpriseId;
        private Long empNo;
        private String department;
        private String empClass;

        public User toEntity(String uuid) {
            return User.builder()
                    .email(this.email)
                    .password(this.password)
                    .name(this.name)
                    .state(State.INACTIVE)
                    .role(Role.ROLE_USER)
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
    public static class EnterpriseRegistDto {
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
    public static class SigninDto {
        private String email;
        private String password;
    }
}
