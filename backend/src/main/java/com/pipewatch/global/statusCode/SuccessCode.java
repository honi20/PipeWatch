package com.pipewatch.global.statusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
	ENTERPRISE_CREATED(HttpStatus.CREATED.value(), "기업 저장에 성공했습니다."),
	USER_CREATED(HttpStatus.CREATED.value(), "유저 저장에 성공했습니다."),
	EMAIL_CODE_SEND_OK(HttpStatus.OK.value(), "이메일 인증 코드 전송에 성공했습니다.")
	;

	private final int httpStatusCode;

	private final String message;
}
