package com.pipewatch.domain.management.service;

import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import com.pipewatch.domain.enterprise.repository.EnterpriseRepository;
import com.pipewatch.domain.management.model.dto.ManagementResponse;
import com.pipewatch.domain.management.repository.ManagementCustomRepository;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.EmployeeRepository;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pipewatch.global.statusCode.ErrorCode.FORBIDDEN_USER_ROLE;
import static com.pipewatch.global.statusCode.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ManagementServiceImpl implements ManagementService {
	private final UserRepository userRepository;
	private final EmployeeRepository employeeRepository;
	private final EnterpriseRepository enterpriseRepository;
	private final ManagementCustomRepository managementCustomRepository;

	@Override
	public ManagementResponse.EmployeeWaitingListDto getWaitingEmployeeList(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 기업 유저만 조회 가능
		if (user.getRole() != Role.ROLE_ENTERPRISE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		Long enterpriseId = enterpriseRepository.findByUserId(user.getId()).getId();

		List<ManagementResponse.EmployeeDto> employees = managementCustomRepository.findPendingEmployeesOfEnterprise(enterpriseId);

		return ManagementResponse.EmployeeWaitingListDto.builder()
				.employees(employees)
				.build();
	}

	@Override
	public ManagementResponse.EmployeeListDto getEmployeeList(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 기업 유저만 조회 가능
		if (user.getRole() != Role.ROLE_ENTERPRISE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		Long enterpriseId = enterpriseRepository.findByUserId(user.getId()).getId();

		List<ManagementResponse.EmployeeDto> employees = managementCustomRepository.findEmployeesOfEnterprise(enterpriseId);

		return ManagementResponse.EmployeeListDto.builder()
				.employees(employees)
				.build();
	}
}
