package com.pipewatch.global.statusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	// Auth
	DUPLICATED_EMAIL(HttpStatus.CONFLICT.value(), "이미 등록된 이메일입니다."),
	SIGNUP_BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "잘못된 회원가입 요청입니다"),
	ENTERPRISE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 기업이 존재하지 않습니다."),
	INVALID_EMAIL_FORMAT(HttpStatus.FORBIDDEN.value(), "선택하신 기업 도메인의 이메일만 등록 가능합니다. {아이디}@{기업 도메인} 형식의 메일로 등록해야 합니다."),
	INVALID_PASSWORD(HttpStatus.FORBIDDEN.value(), "비밀번호가 일치하지 않습니다."),
	EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 이메일로 가입된 회원이 존재하지 않습니다."),
	DUPLICATED_ENTERPRISE(HttpStatus.CONFLICT.value(), "이미 등록된 기업입니다."),

	// mail
	TEMP_PASSWORD_TIMEOUT(HttpStatus.FORBIDDEN.value(), "인증시간이 만료되었습니다."),
	INVALID_EMAIL_CODE(HttpStatus.FORBIDDEN.value(), "인증번호가 일치하지 않습니다."),
	MAIL_SEND_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이메일을 전송하지 못하였습니다."),
	VERIFY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "인증정보가 존재하지 않습니다."),

	// token
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "잘못된 토큰입니다."),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "만료된 토큰입니다"),
	ACCESSTOKEN_REISSUED(HttpStatus.UNAUTHORIZED.value(), "accessToken이 재발급되었습니다."),

	// Management
	ROLE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 Role이 존재하지 않습니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 유저가 존재하지 않습니다."),

	// Pipeline Model
	PIPELINE_MODEL_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 파이프라인 모델이 존재하지 않습니다."),
	FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "파일 업로드에 실패하였습니다."),
	FILE_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "파일 삭제에 실패하였습니다."),
	INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST.value(), "적절하지 않은 파일 확장자입니다."),
	INVALID_FILE_CONTENT(HttpStatus.BAD_REQUEST.value(), "적절하지 않은 파일 형식입니다. meshes 리스트에 각 객체의 name이 존재해야 합니다."),
	PIPELINE_MODEL_MEMO_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 파이프라인 모델의 메모가 존재하지 않습니다."),
	INVALID_IMAGE_FILE_CONTENT(HttpStatus.BAD_REQUEST.value(), "적절하지 않은 이미지 파일 형식입니다. 이미지에서 발견된 파이프가 존재하지 않습니다."),

	// Pipeline
	PIPELINE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 파이프라인이 존재하지 않습니다."),
	MATERIAL_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 파이프라인 재질이 존재하지 않습니다."),

	// Pipe
	PIPE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 파이프가 존재하지 않습니다."),
	PIPE_MEMO_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 파이프 메모가 존재하지 않습니다."),

	// Global
	FORBIDDEN_USER_ROLE(HttpStatus.FORBIDDEN.value(), "접근 권한이 없는 유저입니다."),
	;

	private final int httpStatusCode;

	private final String message;
}
