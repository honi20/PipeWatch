package com.pipewatch.domain.enterprise.controller;

import com.pipewatch.domain.enterprise.model.dto.EnterpriseResponseDto;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pipewatch.global.statusCode.SuccessCode.ENTERPRISE_DETAIL_OK;

@RestController
@RequestMapping("${api_prefix}/enterprises")
@RequiredArgsConstructor
public class EnterpriseController {
	@GetMapping
	public ResponseEntity<?> myPage() {
		EnterpriseResponseDto.DetailDto responseDto = EnterpriseResponseDto.DetailDto.builder()
				.name("ssafy")
				.industry("제조업")
				.managerEmail("paori@ssafy.com")
				.managerPhoneNumber("010-1234-5678")
				.build();

		return new ResponseEntity<>(ResponseDto.success(ENTERPRISE_DETAIL_OK, responseDto), HttpStatus.OK);
	}
}
