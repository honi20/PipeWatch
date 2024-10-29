package com.pipewatch.domain.management.controller;

import com.pipewatch.domain.management.model.dto.ManagementResponse;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pipewatch.global.statusCode.SuccessCode.*;

@RestController
@RequestMapping("${api_prefix}/management")
@RequiredArgsConstructor
public class ManagementController {
	@GetMapping("/waiting-list")
	public ResponseEntity<?> waitingEmployeeList() {
		ManagementResponse.EmployeeDto employee1 = new ManagementResponse.EmployeeDto("김싸피", "kim@ssafy.com", 112345L, "경영지원부", "대리", "사원");
		ManagementResponse.EmployeeDto employee2 = new ManagementResponse.EmployeeDto("최싸피", "choi@ssafy.com", 116789L, "IT개발부", "팀장", "사원");
		ManagementResponse.EmployeeWaitingListDto responseDto = ManagementResponse.EmployeeWaitingListDto.builder()
				.employees(List.of(employee1, employee2))
				.build();

		return new ResponseEntity<>(ResponseDto.success(WAITING_EMPLOYEE_LIST_OK, responseDto), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> employeeList() {
		ManagementResponse.EmployeeDto employee1 = new ManagementResponse.EmployeeDto("이싸피", "lee@ssafy.com", 121212L, "경영지원부", "사원", "사원");
		ManagementResponse.EmployeeDto employee2 = new ManagementResponse.EmployeeDto("박싸피", "park@ssafy.com", 333333L, "IT개발부", "팀장", "관리자");
		ManagementResponse.EmployeeListDto responseDto = ManagementResponse.EmployeeListDto.builder()
				.employees(List.of(employee1, employee2))
				.build();

		return new ResponseEntity<>(ResponseDto.success(EMPLOYEE_LIST_OK, responseDto), HttpStatus.OK);
	}

	@PatchMapping("/{userId}")
	public ResponseEntity<?> userRollModify(@PathVariable Long userId) {
		return new ResponseEntity<>(ResponseDto.success(ROLL_MODIFIED_OK, null), HttpStatus.OK);
	}
}
