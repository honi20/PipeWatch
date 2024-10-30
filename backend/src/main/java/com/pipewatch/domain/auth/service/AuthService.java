package com.pipewatch.domain.auth.service;

import com.pipewatch.domain.auth.model.dto.AuthRequest;
import com.pipewatch.domain.auth.model.dto.AuthResponse;

import java.security.NoSuchAlgorithmException;

public interface AuthService {
    void sendEmailCode(AuthRequest.EmailCodeSendDto requestDto) throws NoSuchAlgorithmException;

    void verifyEmailCode(AuthRequest.EmailCodeVerifyDto requestDto);

    String signup(AuthRequest.SignupDto requestDto);

	AuthResponse.EnterpriseAccountDto registEnterprise(AuthRequest.EnterpriseRegistDto requestDto);
}
