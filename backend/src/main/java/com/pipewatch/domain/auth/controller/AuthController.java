package com.pipewatch.domain.auth.controller;

import com.pipewatch.domain.auth.model.dto.AuthDto;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.pipewatch.global.statusCode.SuccessCode.*;

@RestController
@RequestMapping("${api_prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    @PostMapping("/enterprise")
    public ResponseEntity<?> enterpriseAdd(@RequestBody AuthDto.EnterpriseRegistRequestDto enterpriseRegistRequestDto) {

        return new ResponseEntity<>(ResponseDto.success(ENTERPRISE_CREATED, null), HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody AuthDto.SignupRequestDto signupRequestDto) {
        return new ResponseEntity<>(ResponseDto.success(USER_CREATED, null), HttpStatus.CREATED);
    }

    @PostMapping("/send-email-code")
    public ResponseEntity<?> emailCodeSend(@RequestBody AuthDto.EmailCodeSendRequestDto emailCodeSendRequestDto) {
        return new ResponseEntity<>(ResponseDto.success(EMAIL_CODE_SEND_OK, null), HttpStatus.OK);
    }

    @PostMapping("/verify-email-code")
    public ResponseEntity<?> emailCodeVerify(@RequestBody AuthDto.EmailCodeVerifyRequestDto emailCodeVerifyRequestDto) {
        return new ResponseEntity<>(ResponseDto.success(EMAIL_CODE_VERIFY_OK, null), HttpStatus.OK);
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