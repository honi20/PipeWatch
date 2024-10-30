package com.pipewatch.global.statusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
	// Auth
	EMAIL_CODE_SEND_OK(HttpStatus.OK.value(), "인증코드 발송에 성공했습니다."),
	EMAIL_VERIFIED(HttpStatus.OK.value(), "이메일 인증에 성공했습니다."),
	PASSWORD_RESET_EMAIL_SEND(HttpStatus.OK.value(), "비밀번호 재설정 링크를 이메일로 발송했습니다."),
	PASSWORD_RESET_OK(HttpStatus.OK.value(), "비밀번호 재설정에 성공했습니다."),
	ENTERPRISE_CREATED(HttpStatus.CREATED.value(), "기업 등록에 성공했습니다."),
	USER_CREATED(HttpStatus.CREATED.value(), "회원가입에 성공했습니다."),
	SIGNIN_OK(HttpStatus.OK.value(), "로그인에 성공했습니다."),
	LOGOUT_OK(HttpStatus.OK.value(), "로그아웃되었습니다."),
	ACCESSTOKEN_REISSUED(HttpStatus.CREATED.value(), "accessToken이 재발급되었습니다."),

	// User
	MYPAGE_DETAIL_OK(HttpStatus.OK.value(), "개인정보 조회에 성공했습니다."),
	MYPAGE_MODIFIED_OK(HttpStatus.OK.value(), "개인정보 수정에 성공했습니다."),
	PASSWORD_MODIFIED_OK(HttpStatus.OK.value(), "비밀번호 수정에 성공했습니다."),
	USER_DELETE_OK(HttpStatus.NO_CONTENT.value(), "회원 탈퇴에 성공했습니다."),

	// Enterprise
	ENTERPRISE_DETAIL_OK(HttpStatus.OK.value(), "기업 정보 조회에 성공했습니다."),
	ENTERPRISE_LIST_OK(HttpStatus.OK.value(), "기업 리스트 조회에 성공했습니다."),

	// Management
	WAITING_EMPLOYEE_LIST_OK(HttpStatus.OK.value(), "승인 대기 중인 직원 목록 조회에 성공했습니다."),
	EMPLOYEE_LIST_OK(HttpStatus.OK.value(), "직원 목록 조회에 성공했습니다."),
	ROLL_MODIFIED_OK(HttpStatus.OK.value(), "권한 변경에 성공했습니다."),
	EMPLOYEE_SEARCH_OK(HttpStatus.OK.value(), "사원 검색에 성공했습니다."),
	BUILDING_LIST_OK(HttpStatus.OK.value(), "건물 리스트 조회에 성공했습니다."),

	// Pipeline model
	MODEL_LIST_OK(HttpStatus.OK.value(), "모델 리스트 조회에 성공했습니다."),
	FILE_UPLOAD_AND_MODEL_CREATED(HttpStatus.CREATED.value(), "GLTF 파일 업로드에 성공했습니다. 파이프라인 모델이 생성되었습니다."),
	MODEL_INIT_OK(HttpStatus.OK.value(), "모델 초기 정보 설정에 성공했습니다."),
	MODEL_MODIFIED_OK(HttpStatus.OK.value(), "모델 정보 수정에 성공했습니다."),
	MODEL_DETAIL_OK(HttpStatus.OK.value(), "모델 상세 조회에 성공했습니다."),
	MODEL_DELETED(HttpStatus.NO_CONTENT.value(), "모델 삭제에 성공했습니다."),

	// Pipeline
	PIPELINE_DETAIL_OK(HttpStatus.OK.value(), "단일 파이프라인 조회에 성공했습니다."),
	PIPELINE_MODIFIED_OK(HttpStatus.OK.value(), "단일 파이프라인 기본 정보 수정에 성공했습니다."),
	PIPELINE_PROPERTY_MODIFIED_OK(HttpStatus.OK.value(), "단일 파이프라인 속성 정보 수정에 성공했습니다."),
	PIPELINE_MODELING_CREATED(HttpStatus.CREATED.value(), "파이프라인 모델링이 생성되었습니다."),
	
	// Pipe
	PIPE_MEMO_LIST_OK(HttpStatus.OK.value(), "파이프 메모 리스트 조회에 성공했습니다."),
	PIPE_MEMO_CREATED(HttpStatus.CREATED.value(), "파이프 메모가 생성되었습니다."),
	PIPE_MEMO_DELETED(HttpStatus.NO_CONTENT.value(), "메모 삭제에 성공했습니다."),
	;

	private final int httpStatusCode;

	private final String message;
}
