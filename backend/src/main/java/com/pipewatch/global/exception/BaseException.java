package com.pipewatch.global.exception;

import com.pipewatch.global.statusCode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
	ErrorCode errorCode;
}
