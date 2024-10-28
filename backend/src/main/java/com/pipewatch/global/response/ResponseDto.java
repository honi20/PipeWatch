package com.pipewatch.global.response;

import com.pipewatch.global.statusCode.ErrorCode;
import com.pipewatch.global.statusCode.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
	private ResponseHeader header;

	private T body;

	public static <T> ResponseDto<T> success(SuccessCode s, T body) {
		return new ResponseDto<>(new ResponseHeader(s), body);
	}

	public static <T> ResponseDto<T> fail(ErrorCode e) {
		return new ResponseDto<>(new ResponseHeader(e), null);
	}

	public static <T> ResponseDto<T> fail(String errorMessage) {
		return new ResponseDto<>(new ResponseHeader(HttpStatus.BAD_REQUEST.value(), errorMessage), null);
	}
}
