package com.pipewatch.domain.auth.controller;

import com.pipewatch.domain.auth.model.dto.AuthDto;
import com.pipewatch.domain.auth.service.AuthService;
import com.pipewatch.global.jwt.service.JwtService;
import com.pipewatch.global.redis.RedisUtil;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

import static com.pipewatch.global.statusCode.SuccessCode.*;

@RestController
@RequestMapping("${api_prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final RedisUtil redisUtil;

    @PostMapping("/send-email-code")
    public ResponseEntity<?> emailCodeSend(@RequestBody AuthDto.EmailCodeSendRequestDto requestDto) throws NoSuchAlgorithmException {
        authService.sendEmailCode(requestDto);

        return new ResponseEntity<>(ResponseDto.success(EMAIL_CODE_SEND_OK, null), HttpStatus.OK);
    }

    @PostMapping("/verify-email-code")
    public ResponseEntity<?> emailCodeVerify(@RequestBody AuthDto.EmailCodeVerifyRequestDto requestDto) {
        authService.verifyEmailCode(requestDto);

        return new ResponseEntity<>(ResponseDto.success(EMAIL_VERIFIED, null), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody AuthDto.SignupRequestDto requestDto) {
        String accessToken = authService.signup(requestDto);

        AuthDto.AccessTokenResponseDto responseDto
                = AuthDto.AccessTokenResponseDto.builder().accessToken(accessToken).build();

        return new ResponseEntity<>(ResponseDto.success(USER_CREATED, responseDto), HttpStatus.CREATED);
    }

    @PostMapping("/enterprise")
    public ResponseEntity<?> enterpriseAdd(@RequestBody AuthDto.EnterpriseRegistRequestDto enterpriseRegistRequestDto) {

        return new ResponseEntity<>(ResponseDto.success(ENTERPRISE_CREATED, null), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AuthDto.SigninRequestDto signinRequestDto) {
        return new ResponseEntity<>(ResponseDto.success(SIGNIN_OK, null), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return new ResponseEntity<>(ResponseDto.success(LOGOUT_OK, null), HttpStatus.OK);
    }
}