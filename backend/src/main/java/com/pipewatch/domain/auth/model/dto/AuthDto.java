package com.pipewatch.domain.auth.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthDto {
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
    public static class SignupRequestDto {
        private String email;
        private String password;
        private String name;
        private Long enterpriseId;
        private Long empNo;
        private String department;
        private String empClass;
    }

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
    public static class SigninRequestDto {
        private String email;
        private String password;
    }
}
