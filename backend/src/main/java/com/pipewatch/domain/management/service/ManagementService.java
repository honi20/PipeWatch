package com.pipewatch.domain.management.service;

import com.pipewatch.domain.management.model.dto.ManagementResponse;

public interface ManagementService {
	ManagementResponse.EmployeeWaitingListDto getWaitingEmployeeList(Long userId);
}
