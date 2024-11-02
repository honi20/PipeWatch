package com.pipewatch.domain.management.service;

import com.pipewatch.domain.enterprise.model.entity.BuildingAndFloor;
import com.pipewatch.domain.enterprise.repository.BuildingRepository;
import com.pipewatch.domain.enterprise.repository.EnterpriseRepository;
import com.pipewatch.domain.management.model.dto.ManagementRequest;
import com.pipewatch.domain.management.model.dto.ManagementResponse;
import com.pipewatch.domain.management.repository.ManagementCustomRepository;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.State;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.EmployeeRepository;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pipewatch.global.statusCode.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ManagementServiceImpl implements ManagementService {
	private final UserRepository userRepository;
	private final BuildingRepository buildingRepository;
	private final EnterpriseRepository enterpriseRepository;
	private final ManagementCustomRepository managementCustomRepository;
	private final EmployeeRepository employeeRepository;

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

		Long enterpriseId = null;
		if (user.getRole() == Role.ROLE_ENTERPRISE) {
			enterpriseId = enterpriseRepository.findByUserId(user.getId()).getId();
		}
		else if (user.getRole() == Role.ROLE_EMPLOYEE || user.getRole() == Role.ROLE_ADMIN) {
			enterpriseId = employeeRepository.findByUserId(user.getId()).getEnterprise().getId();
		}

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
		employee.updateState(State.ACTIVE);
		userRepository.save(employee);
	}

	@Override
	public ManagementResponse.EmployeeSearchDto searchEmployee(Long userId, String keyword) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		Long enterpriseId = null;
		if (user.getRole() == Role.ROLE_ENTERPRISE) {
			enterpriseId = enterpriseRepository.findByUserId(user.getId()).getId();
		}
		else if (user.getRole() == Role.ROLE_EMPLOYEE || user.getRole() == Role.ROLE_ADMIN) {
			enterpriseId = employeeRepository.findByUserId(user.getId()).getEnterprise().getId();
		}

		List<ManagementResponse.EmployeeDto> employees = managementCustomRepository.findEmployeesOfEnterpriseByKeyword(enterpriseId, keyword);

		return ManagementResponse.EmployeeSearchDto.builder()
				.employees(employees)
				.build();
	}

	@Override
	public ManagementResponse.BuildingListDto getBuildingList(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		Long enterpriseId = null;
		if (user.getRole() == Role.ROLE_ENTERPRISE) {
			enterpriseId = enterpriseRepository.findByUserId(user.getId()).getId();
		}
		else if (user.getRole() == Role.ROLE_EMPLOYEE || user.getRole() == Role.ROLE_ADMIN) {
			enterpriseId = employeeRepository.findByUserId(user.getId()).getEnterprise().getId();
		}

		List<BuildingAndFloor> buildings = buildingRepository.findByEnterpriseId(enterpriseId);

		// name으로 그룹화하고 floor를 오름차순으로 정렬하여 리스트로 수집
		Map<String, List<Integer>> groupedFloors = buildings.stream()
				.collect(Collectors.groupingBy(
						BuildingAndFloor::getName,
						Collectors.collectingAndThen(
								Collectors.mapping(BuildingAndFloor::getFloor, Collectors.toList()),
								floors -> floors.stream().sorted().collect(Collectors.toList())
						)
				));

		// 그룹화된 데이터를 BuildingDto 리스트로 변환
		List<ManagementResponse.BuildingDto> buildingDtos = groupedFloors.entrySet().stream()
				.map(entry -> new ManagementResponse.BuildingDto(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());

		return ManagementResponse.BuildingListDto.builder()
				.buildings(buildingDtos)
				.build();
	}
}
