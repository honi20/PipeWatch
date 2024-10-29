package com.pipewatch.global.jwt.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "jwt_token")
public class JwtToken {
    @Id
    private String uuid;

    private Long userId;

    private String refreshToken;

    private String verify;
}
