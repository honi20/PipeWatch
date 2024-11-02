package com.pipewatch.domain.management.controller;

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

	@PatchMapping("/{userUuid}")
	public ResponseEntity<?> userRollModify(@PathVariable String userUuid) {
		return new ResponseEntity<>(ResponseDto.success(ROLE_MODIFIED_OK, null), HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<?> employeeDetail(@RequestParam(required = false) String keyword) {
		ManagementResponse.EmployeeDto employee1 = new ManagementResponse.EmployeeDto("qkepvmdfljsfj", "이싸피", "lee@ssafy.com", 121212L, "경영지원부", "사원", "사원");
		ManagementResponse.EmployeeDto employee2 = new ManagementResponse.EmployeeDto("dlkjfwpcjlsdf", "박싸피", "park@ssafy.com", 333333L, "IT개발부", "팀장", "관리자");
		ManagementResponse.EmployeeSearchDto responseDto = ManagementResponse.EmployeeSearchDto.builder()
				.employees(List.of(employee1, employee2))
				.build();

		return new ResponseEntity<>(ResponseDto.success(EMPLOYEE_SEARCH_OK, responseDto), HttpStatus.OK);
	}

	@GetMapping("/buildings")
	public ResponseEntity<?> buildingList() {
		ManagementResponse.BuildingDto building1 = new ManagementResponse.BuildingDto("역삼 멀티캠퍼스", List.of(12, 14, 15));
		ManagementResponse.BuildingDto building2 = new ManagementResponse.BuildingDto("부울경 멀티캠퍼스", List.of(1, 2, 3));
		ManagementResponse.BuildingListDto responseDto = ManagementResponse.BuildingListDto.builder()
				.buildings(List.of(building1, building2))
				.build();

		return new ResponseEntity<>(ResponseDto.success(BUILDING_LIST_OK, responseDto), HttpStatus.OK);
	}

}
