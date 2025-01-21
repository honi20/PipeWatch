package com.pipewatch.domain.enterprise.controller;

import com.pipewatch.domain.enterprise.model.dto.EnterpriseResponse;
import com.pipewatch.domain.enterprise.service.EnterpriseService;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pipewatch.global.statusCode.SuccessCode.*;

@RestController
@RequestMapping("${api_prefix}/enterprises")
@RequiredArgsConstructor
public class EnterpriseController implements EnterpriseApiSwagger {
	private final EnterpriseService enterpriseService;

	@GetMapping("/detail")
	public ResponseEntity<?> enterpriseDetail(@AuthenticationPrincipal Long userId) {
		EnterpriseResponse.DetailDto responseDto = enterpriseService.getEnterpriseDetail(userId);

		return new ResponseEntity<>(ResponseDto.success(ENTERPRISE_DETAIL_OK, responseDto), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> enterpriseList() {
		EnterpriseResponse.ListDto responseDto = enterpriseService.getEnterpriseList();

		return new ResponseEntity<>(ResponseDto.success(ENTERPRISE_LIST_OK, responseDto), HttpStatus.OK);
	}

	@GetMapping("/buildings")
	public ResponseEntity<?> buildingList(@AuthenticationPrincipal Long userId) {
		EnterpriseResponse.BuildingListDto responseDto = enterpriseService.getBuildingList(userId);

		return new ResponseEntity<>(ResponseDto.success(BUILDING_LIST_OK, responseDto), HttpStatus.OK);
	}

	@GetMapping("/buildings/floors")
	public ResponseEntity<?> buildingAndFloorList(@AuthenticationPrincipal Long userId) {
		EnterpriseResponse.BuildingAndFloorListDto responseDto = enterpriseService.getBuildingAndFloorList(userId);

		return new ResponseEntity<>(ResponseDto.success(BUILDING_AND_FLOOR_LIST_OK, responseDto), HttpStatus.OK);
	}
}
