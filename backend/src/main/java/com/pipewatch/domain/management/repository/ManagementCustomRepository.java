package com.pipewatch.domain.management.repository;

import com.pipewatch.domain.enterprise.model.entity.BuildingAndFloor;
import com.pipewatch.domain.management.model.dto.ManagementResponse;

import java.util.List;

public interface ManagementCustomRepository {
	List<ManagementResponse.EmployeeDto> findPendingEmployeesOfEnterprise(Long enterpriseId);

	List<ManagementResponse.EmployeeDto> findEmployeesOfEnterprise(Long enterpriseId);

	List<ManagementResponse.EmployeeDto> findEmployeesOfEnterpriseByKeyword(Long enterpriseId, String keyword);
}
