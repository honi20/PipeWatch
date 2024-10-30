package com.pipewatch.domain.auth.controller;

import com.pipewatch.domain.auth.model.dto.AuthRequest;
import com.pipewatch.domain.auth.model.dto.AuthResponse;
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

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody AuthRequest.SignupDto requestDto) throws NoSuchAlgorithmException {
        String accessToken = authService.signup(requestDto);

        AuthResponse.AccessTokenDto responseDto
                = AuthResponse.AccessTokenDto.builder().accessToken(accessToken).build();

        return new ResponseEntity<>(ResponseDto.success(USER_CREATED, responseDto), HttpStatus.CREATED);
    }

    @GetMapping("/verify-email-code")
    public ResponseEntity<?> emailCodeVerify(@RequestParam("token") String token) {
        authService.verifyEmailCode(token);

        String redirectUrl = "https://www.google.co.kr/?hl=ko";
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", redirectUrl).build();
    }

    @PostMapping("/enterprise")
    public ResponseEntity<?> enterpriseAdd(@RequestBody AuthRequest.EnterpriseRegistDto enterpriseRegistRequestDto) {

        return new ResponseEntity<>(ResponseDto.success(ENTERPRISE_CREATED, null), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AuthRequest.SigninDto signinRequestDto) {
        return new ResponseEntity<>(ResponseDto.success(SIGNIN_OK, null), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return new ResponseEntity<>(ResponseDto.success(LOGOUT_OK, null), HttpStatus.OK);
    }
}