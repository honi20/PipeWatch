package com.pipewatch.global.statusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	DUPLICATE_EMAIL(HttpStatus.CONFLICT.value(), "중복된 이메일입니다."),
	INVALID_EMAIL_CODE(HttpStatus.FORBIDDEN.value(), "인증에 실패했습니다. 인증번호를 확인해 주세요."),
	EMAIL_NOT_FOUNT(HttpStatus.NOT_FOUND.value(), "입력받은 이메일로 가입된 회원을 찾을 수 없습니다."),
	INVALID_PASSWORD(HttpStatus.FORBIDDEN.value(), "이메일과 비밀번호가 일치하지 않습니다."),
	FORBIDDEN_USER_ROLL(HttpStatus.FORBIDDEN.value(), "접근 권한이 없는 유저입니다."),
	INVALID_ROLL(HttpStatus.NOT_FOUND.value(), "입력된 Roll이 존재하지 않습니다.")
	;

	private final int httpStatusCode;

	private final String message;
}
