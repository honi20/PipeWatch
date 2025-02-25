package com.pipewatch.domain.auth.controller;

import com.pipewatch.domain.auth.model.dto.AuthRequest;
import com.pipewatch.domain.auth.model.dto.AuthResponse;
import com.pipewatch.domain.auth.service.AuthService;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

import static com.pipewatch.global.statusCode.SuccessCode.*;

@RestController
@RequestMapping("${api_prefix}/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApiSwagger {
	private final AuthService authService;

	@PostMapping("/email-code/send")
	public ResponseEntity<?> emailCodeSend(@RequestBody AuthRequest.EmailCodeSendDto requestDto) throws NoSuchAlgorithmException {
		authService.sendEmailCode(requestDto);

		return new ResponseEntity<>(ResponseDto.success(EMAIL_CODE_SEND_OK, null), HttpStatus.OK);
	}

	@PostMapping("/email-code/verify")
	public ResponseEntity<?> emailCodeVerify(@RequestBody AuthRequest.EmailCodeVerifyDto requestDto) {
		authService.verifyEmailCode(requestDto);

		return new ResponseEntity<>(ResponseDto.success(EMAIL_VERIFIED, null), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> signup(@RequestBody AuthRequest.SignupDto requestDto) {
		AuthResponse.AccessTokenDto responseDto = authService.signup(requestDto);

		return new ResponseEntity<>(ResponseDto.success(USER_CREATED, responseDto), HttpStatus.CREATED);
	}

	@PostMapping("/enterprise")
	public ResponseEntity<?> enterpriseAdd(@RequestBody AuthRequest.EnterpriseRegistDto requestDto) throws NoSuchAlgorithmException {
		authService.registEnterprise(requestDto);

		return new ResponseEntity<>(ResponseDto.success(ENTERPRISE_CREATED, null), HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<?> signin(@RequestBody AuthRequest.SigninDto requestDto) {
		AuthResponse.AccessTokenDto responseDto = authService.signin(requestDto);

		return new ResponseEntity<>(ResponseDto.success(SIGNIN_OK, responseDto), HttpStatus.OK);
	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout(@AuthenticationPrincipal Long userId) {
		authService.logout(userId);

		return new ResponseEntity<>(ResponseDto.success(LOGOUT_OK, null), HttpStatus.OK);
	}

	@PostMapping("/password-reset/request")
	public ResponseEntity<?> passwordResetEmailSend(@RequestBody AuthRequest.EmailPwdSendDto requestDto) {
		authService.sendPasswordResetEmail(requestDto);

		return new ResponseEntity<>(ResponseDto.success(PASSWORD_RESET_EMAIL_SEND, null), HttpStatus.OK);
	}

	@PostMapping("/password-reset")
	public ResponseEntity<?> passwordReset(@RequestBody AuthRequest.PasswordResetDto requestDto) {
		authService.resetPassword(requestDto);

		return new ResponseEntity<>(ResponseDto.success(PASSWORD_RESET_OK, null), HttpStatus.OK);
	}
}