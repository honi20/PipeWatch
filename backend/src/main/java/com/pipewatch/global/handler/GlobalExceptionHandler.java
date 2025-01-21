package com.pipewatch.global.handler;

import com.pipewatch.global.exception.BaseException;
import com.pipewatch.global.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ResponseDto<?>> handleException(Exception e) {
		return new ResponseEntity<>(ResponseDto.fail(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(BaseException.class)
	protected ResponseEntity<ResponseDto<?>> handleCustomException(BaseException e) {
		return new ResponseEntity<>(ResponseDto.fail(e.getErrorCode()), HttpStatus.valueOf(e.getErrorCode().getHttpStatusCode()));
	}

}
