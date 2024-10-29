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
	MYPAGE_DETAIL_OK(HttpStatus.OK.value(), "개인정보 조회에 성공했습니다."),
	MYPAGE_MODIFIED_OK(HttpStatus.OK.value(), "개인정보 수정에 성공했습니다."),
	PASSWORD_MODIFIED_OK(HttpStatus.OK.value(), "비밀번호 수정에 성공했습니다."),
	USER_DELETE_OK(HttpStatus.NO_CONTENT.value(), "회원 탈퇴에 성공했습니다."),
	ENTERPRISE_DETAIL_OK(HttpStatus.OK.value(), "기업 정보 조회에 성공했습니다."),
	WAITING_EMPLOYEE_LIST_OK(HttpStatus.OK.value(), "승인 대기 중인 직원 목록 조회에 성공했습니다."),
	EMPLOYEE_LIST_OK(HttpStatus.OK.value(), "직원 목록 조회에 성공했습니다."),
	ROLL_MODIFIED_OK(HttpStatus.OK.value(), "권한 변경에 성공했습니다.")
	;

	private final int httpStatusCode;

	private final String message;
}
