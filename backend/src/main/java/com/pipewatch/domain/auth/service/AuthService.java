package com.pipewatch.domain.auth.service;

import com.pipewatch.domain.auth.model.dto.AuthRequest;

import java.security.NoSuchAlgorithmException;

public interface AuthService {
    String signup(AuthRequest.SignupDto requestDto) throws NoSuchAlgorithmException;

    void verifyEmailCode(String token);
}
