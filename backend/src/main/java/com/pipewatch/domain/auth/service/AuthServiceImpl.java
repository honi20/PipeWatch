package com.pipewatch.domain.auth.service;

import com.pipewatch.domain.auth.model.dto.AuthDto;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import com.pipewatch.global.jwt.entity.JwtToken;
import com.pipewatch.global.jwt.service.JwtService;
import com.pipewatch.global.mail.MailService;
import com.pipewatch.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

import static com.pipewatch.global.statusCode.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final RedisUtil redisUtil;

    private final MailService mailService;

    @Override
    public void sendEmailCode(AuthDto.EmailCodeSendRequestDto requestDto) throws NoSuchAlgorithmException {
        User user = userRepository.findByEmail(requestDto.getEmail());

        if (user == null) {
            String verifyCode = mailService.sendVerifyEmail(requestDto.getEmail());

            JwtToken jwtToken = JwtToken.builder().verify(verifyCode).build();
            redisUtil.setDataWithExpiration(requestDto.getEmail() + "_verify", jwtToken, 1000L);
            return;
        }
        throw new BaseException(DUPLICATE_EMAIL);
    }
}
