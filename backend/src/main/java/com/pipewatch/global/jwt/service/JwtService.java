package com.pipewatch.global.jwt.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

public interface JwtService {
	String createAccessToken(String uuid);

	String createRefreshToken(String uuid);

	String getClaim(String token);

	String getUuid(String token);

	Authentication getAuthentication(String accessToken);

	boolean isTokenValid(String token);

	boolean isTokenExpired(String token);

	Long getUserId(SecurityContext securityContext);
}
