package com.pipewatch.domain.auth.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthResponse {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccessTokenDto {
        private String accessToken;
    }
}
