package com.pipewatch.domain.management.controller;

import com.pipewatch.domain.management.model.dto.ManagementRequest;
import com.pipewatch.domain.management.model.dto.ManagementResponse;
import com.pipewatch.domain.management.service.ManagementService;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pipewatch.global.statusCode.SuccessCode.*;

@RestController
@RequestMapping("${api_prefix}/management")
@RequiredArgsConstructor
public class ManagementController {
	private final ManagementService managementService;

	@GetMapping("/waiting-list")
	public ResponseEntity<?> waitingEmployeeList(@AuthenticationPrincipal Long userId) {
		ManagementResponse.EmployeeWaitingListDto responseDto = managementService.getWaitingEmployeeList(userId);

		return new ResponseEntity<>(ResponseDto.success(WAITING_EMPLOYEE_LIST_OK, responseDto), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> employeeList(@AuthenticationPrincipal Long userId) {
		ManagementResponse.EmployeeListDto responseDto = managementService.getEmployeeList(userId);

		return new ResponseEntity<>(ResponseDto.success(EMPLOYEE_LIST_OK, responseDto), HttpStatus.OK);
	}

	@PatchMapping
	public ResponseEntity<?> userRollModify(@AuthenticationPrincipal Long userId, @RequestBody ManagementRequest.AccessModifyDto requestDto) {
		managementService.modifyUserRoll(userId, requestDto);

		return new ResponseEntity<>(ResponseDto.success(ROLE_MODIFIED_OK, null), HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<?> employeeDetail(@AuthenticationPrincipal Long userId, @RequestParam(required = false) String keyword) {
		ManagementResponse.EmployeeSearchDto responseDto = managementService.searchEmployee(userId, keyword);

		return new ResponseEntity<>(ResponseDto.success(EMPLOYEE_SEARCH_OK, responseDto), HttpStatus.OK);
	}

}
