package com.pipewatch.domain.management.service;

import com.pipewatch.domain.enterprise.repository.EnterpriseRepository;
import com.pipewatch.domain.management.model.dto.ManagementRequest;
import com.pipewatch.domain.management.model.dto.ManagementResponse;
import com.pipewatch.domain.management.repository.ManagementCustomRepository;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.EmployeeRepository;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.pipewatch.global.statusCode.ErrorCode.*;

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

	@Override
	@Transactional
	public void modifyUserRoll(Long userId, ManagementRequest.AccessModifyDto requestDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 기업 유저만 조회 가능
		if (user.getRole() != Role.ROLE_ENTERPRISE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		User employee = userRepository.findByUuid(requestDto.getUserUuid());
		if (employee == null) {
			throw new BaseException(USER_NOT_FOUND);
		}

		// 존재하는 Role인지 확인
		if (!Role.isValidRole(requestDto.getNewRole())) {
			throw new BaseException(ROLE_NOT_FOUND);
		}

		employee.updateRole(Role.valueOf(requestDto.getNewRole()));
		userRepository.save(employee);
	}
}
