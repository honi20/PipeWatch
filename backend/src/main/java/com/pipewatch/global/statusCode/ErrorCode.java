package com.pipewatch.global.statusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	DUPLICATE_EMAIL(HttpStatus.CONFLICT.value(), "중복된 이메일입니다."),
	INVALID_EMAIL_CODE(HttpStatus.FORBIDDEN.value(), "잘못된 이메일 인증 코드입니다.")
	;

	private final int httpStatusCode;

	private final String message;
}
