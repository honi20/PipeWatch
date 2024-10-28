package com.pipewatch.global.statusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
	SUCCESS_TEST_OK(HttpStatus.OK.value(), "테스트에 성공했습니다.")
	;

	private final int httpStatusCode;

	private final String message;
}
