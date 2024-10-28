package com.pipewatch.global.statusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	ERROR_TEST_OK(HttpStatus.FORBIDDEN.value(), "에러 테스트에 성공했습니다.")
	;

	private final int httpStatusCode;

	private final String message;
}
