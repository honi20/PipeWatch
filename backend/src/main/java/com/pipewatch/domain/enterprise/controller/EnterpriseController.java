package com.pipewatch.domain.enterprise.controller;

import com.pipewatch.domain.enterprise.model.dto.EnterpriseResponse;
import com.pipewatch.domain.enterprise.service.EnterpriseService;
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
	private final EnterpriseService enterpriseService;

	@GetMapping
	public ResponseEntity<?> enterpriseDetail() {
		EnterpriseResponse.DetailDto responseDto = enterpriseService.detailEnterprise();

		return new ResponseEntity<>(ResponseDto.success(ENTERPRISE_DETAIL_OK, responseDto), HttpStatus.OK);
	}
}
