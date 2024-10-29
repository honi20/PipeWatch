package com.pipewatch.global.statusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
	ENTERPRISE_CREATED(HttpStatus.CREATED.value(), "기업 등록에 성공했습니다."),
	USER_CREATED(HttpStatus.CREATED.value(), "유저 생성에 성공했습니다."),
	EMAIL_CODE_SEND_OK(HttpStatus.OK.value(), "이메일 인증 코드를 해당 이메일로 전송했습니다."),
	EMAIL_CODE_VERIFY_OK(HttpStatus.OK.value(), "이메일 인증에 성공했습니다."),
	SIGNIN_OK(HttpStatus.OK.value(), "로그인에 성공했습니다."),
	LOGOUT_OK(HttpStatus.OK.value(), "로그아웃되었습니다."),
	MYPAGE_OK(HttpStatus.OK.value(), "개인정보 조회에 성공했습니다.")
	;

	private final int httpStatusCode;

	private final String message;
}
