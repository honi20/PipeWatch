package com.pipewatch.global.statusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	// Auth
	DUPLICATE_EMAIL(HttpStatus.CONFLICT.value(), "중복된 이메일입니다."),
	INVALID_EMAIL_CODE(HttpStatus.FORBIDDEN.value(), "인증에 실패했습니다. 인증번호를 확인해 주세요."),
	EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 이메일로 가입된 회원이 존재하지 않습니다."),
	INVALID_PASSWORD(HttpStatus.FORBIDDEN.value(), "이메일과 비밀번호가 일치하지 않습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "잘못된 토큰입니다."),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "만료된 토큰입니다"),

	// Management
	ROLL_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 Roll이 존재하지 않습니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 유저가 존재하지 않습니다."),

	// Pipeline Model
	PIPELINE_MODEL_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 파이프라인 모델이 존재하지 않습니다."),

	// Pipeline
	PIPELINE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 파이프라인이 존재하지 않습니다."),

	// Pipe
	PIPE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 파이프가 존재하지 않습니다."),

	// Global
	FORBIDDEN_USER_ROLL(HttpStatus.FORBIDDEN.value(), "접근 권한이 없는 유저입니다."),
	;

	private final int httpStatusCode;

	private final String message;
}
