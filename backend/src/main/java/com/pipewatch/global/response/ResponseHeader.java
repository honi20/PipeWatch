package com.pipewatch.global.response;

import com.pipewatch.global.statusCode.ErrorCode;
import com.pipewatch.global.statusCode.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseHeader {
	private int httpStatusCode;

	private String message;

	public ResponseHeader(SuccessCode s) {
		this.httpStatusCode = s.getHttpStatusCode();
		this.message = s.getMessage();
	}

	public ResponseHeader(ErrorCode e) {
		this.httpStatusCode = e.getHttpStatusCode();
		this.message = e.getMessage();
	}
}
