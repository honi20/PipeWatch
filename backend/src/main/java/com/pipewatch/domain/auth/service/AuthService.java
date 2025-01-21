package com.pipewatch.domain.auth.service;

import com.pipewatch.domain.auth.model.dto.AuthRequest;
import com.pipewatch.domain.auth.model.dto.AuthResponse;

import java.security.NoSuchAlgorithmException;

public interface AuthService {
	void sendEmailCode(AuthRequest.EmailCodeSendDto requestDto) throws NoSuchAlgorithmException;

	void verifyEmailCode(AuthRequest.EmailCodeVerifyDto requestDto);

	AuthResponse.AccessTokenDto signup(AuthRequest.SignupDto requestDto);

	void registEnterprise(AuthRequest.EnterpriseRegistDto requestDto) throws NoSuchAlgorithmException;

	AuthResponse.AccessTokenDto signin(AuthRequest.SigninDto requestDto);

	void logout(Long userId);

	void sendPasswordResetEmail(AuthRequest.EmailPwdSendDto requestDto);

	void resetPassword(AuthRequest.PasswordResetDto requestDto);
}
