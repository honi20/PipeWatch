package com.pipewatch.domain.management.service;

import com.pipewatch.domain.management.model.dto.ManagementRequest;
import com.pipewatch.domain.management.model.dto.ManagementResponse;

public interface ManagementService {
	ManagementResponse.EmployeeWaitingListDto getWaitingEmployeeList(Long userId);

	ManagementResponse.EmployeeListDto getEmployeeList(Long userId);

	void modifyUserRoll(Long userId, ManagementRequest.AccessModifyDto requestDto);

	ManagementResponse.EmployeeSearchDto searchEmployee(Long userId, String keyword);
}
