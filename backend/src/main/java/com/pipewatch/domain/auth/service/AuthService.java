package com.pipewatch.domain.auth.service;

import com.pipewatch.domain.auth.model.dto.AuthDto;

import java.security.NoSuchAlgorithmException;

public interface AuthService {
    void sendEmailCode(AuthDto.EmailCodeSendRequestDto requestDto) throws NoSuchAlgorithmException;

    void verifyEmailCode(AuthDto.EmailCodeVerifyRequestDto requestDto);
}
