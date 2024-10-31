package com.pipewatch.domain.management.repository;

import com.pipewatch.domain.management.model.dto.ManagementResponse;

import java.util.List;

public interface ManagementCustomRepository {
	List<ManagementResponse.EmployeeDto> findPendingEmployeesOfEnterprise(Long enterpriseId);
}
