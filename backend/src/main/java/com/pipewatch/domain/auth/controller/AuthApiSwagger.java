package com.pipewatch.domain.auth.controller;

import com.pipewatch.domain.auth.model.dto.AuthRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.NoSuchAlgorithmException;

@Tag(name = "Auth API", description = "Auth API Document")
public interface AuthApiSwagger {
	@PostMapping("/send-email-code")
	@Operation(summary = "메일 인증번호 전송")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "메일 인증코드 전송 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"인증코드 발송에 성공했습니다.\"},\n\"body\": null}")}
			)),
			@ApiResponse(responseCode = "409", description = "메일 인증코드 전송 실패 - 이미 등록된 메일", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 409, \"message\": \"이미 등록된 이메일입니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> emailCodeSend(@RequestBody AuthRequest.EmailCodeSendDto requestDto
	) throws NoSuchAlgorithmException;

	@PostMapping("/verify-email-code")
	@Operation(summary = "메일 인증번호 확인")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "메일 인증코드 확인 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"이메일 인증에 성공했습니다.\"},\n\"body\": null}")}
			)),
			@ApiResponse(responseCode = "403", description = "메일 인증코드 확인 실패 - 잘못된 인증코드", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 403, \"message\": \"인증번호가 일치하지 않습니다.\"},\n\"body\": null}")}
			)),
			@ApiResponse(responseCode = "404", description = "메일 인증코드 확인 실패 - 인증정보가 존재하지 않는 이메일", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 404, \"message\": \"인증정보가 존재하지 않습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> emailCodeVerify(@RequestBody AuthRequest.EmailCodeVerifyDto requestDto);

	@PostMapping
	@Operation(summary = "회원가입", description = "gmail/naver은 ssafy 기업 메일이라고 가정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 201, \"message\": \"회원가입에 성공했습니다.\"},\n\"body\": {\"accessToken\": \"sampleAccessToken\"}}")}
			)),
			@ApiResponse(responseCode = "403", description = "회원가입 실패 - 잘못된 이메일 형식 (기업 도메인과 형식이 같아야 함)", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 403, \"message\": \"선택하신 기업 도메인의 이메일만 등록 가능합니다. {아이디}@{기업 도메인} 형식의 메일로 등록해야 합니다.\"},\n\"body\": null}")}
			)),
			@ApiResponse(responseCode = "404", description = "회원가입 실패 - 존재하지 않는 기업", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 404, \"message\": \"해당 기업이 존재하지 않습니다.\"},\n\"body\": null}")}
			)),
			@ApiResponse(responseCode = "409", description = "회원가입 실패 - 이미 등록된 이메일", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 409, \"message\": \"이미 등록된 이메일입니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> signup(@RequestBody AuthRequest.SignupDto requestDto);

	@PostMapping("/enterprise")
	@Operation(summary = "기업 가입", description = "gmail/naver은 ssafy 기업 메일이라고 가정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "기업 등록 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 201, \"message\": \"기업 등록에 성공했습니다.\"},\n\"body\": null}")}
			)),
			@ApiResponse(responseCode = "409", description = "기업 등록 실패 - 이미 등록된 기업", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 409, \"message\": \"이미 등록된 기업입니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> enterpriseAdd(@RequestBody AuthRequest.EnterpriseRegistDto requestDto) throws NoSuchAlgorithmException;

	@PostMapping("/signin")
	@Operation(summary = "로그인")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"로그인에 성공했습니다.\"},\n\"body\": {\"accessToken\": \"sampleAccessToken\"}}")}
			)),
			@ApiResponse(responseCode = "403", description = "로그인 실패 - 잘못된 비밀번호", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 403, \"message\": \"비밀번호가 일치하지 않습니다.\"},\n\"body\": null}")}
			)),
			@ApiResponse(responseCode = "404", description = "로그인 실패 - 존재하지 않는 이메일", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 404, \"message\": \"해당 이메일로 가입된 회원이 존재하지 않습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> signin(@RequestBody AuthRequest.SigninDto requestDto);

	@GetMapping("/logout")
	@Operation(summary = "로그아웃")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"로그아웃되었습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> logout(@AuthenticationPrincipal Long userId);

	@PostMapping("/send-pwd-reset")
	@Operation(summary = "비밀번호 재설정 메일 전송")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "비밀번호 재설정 메일 전송 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"비밀번호 재설정 링크를 이메일로 발송했습니다.\"},\n\"body\": null}")}
			)),
			@ApiResponse(responseCode = "404", description = "비밀번호 재설정 메일 전송 실패 - 존재하지 않는 유저", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 404, \"message\": \"해당 유저가 존재하지 않습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> passwordResetEmailSend(@RequestBody AuthRequest.EmailPwdSendDto requestDto);

	@PostMapping("/reset-pwd")
	@Operation(summary = "비밀번호 재설정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "비밀번호 재설정 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 200, \"message\": \"비밀번호 재설정에 성공했습니다.\"},\n\"body\": null}")}
			)),
			@ApiResponse(responseCode = "404", description = "비밀번호 재설정 실패 - 존재하지 않는 이메일", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					examples = {@ExampleObject(value = "{\"header\":{\"httpStatusCode\": 404, \"message\": \"인증정보가 존재하지 않습니다.\"},\n\"body\": null}")}
			))
	})
	ResponseEntity<?> passwordReset(@RequestBody AuthRequest.PasswordResetDto requestDto);
}
