package com.pipewatch.domain.enterprise.service;

import com.pipewatch.domain.enterprise.model.dto.EnterpriseResponse;
import com.pipewatch.domain.enterprise.model.entity.BuildingAndFloor;
import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import com.pipewatch.domain.enterprise.repository.BuildingRepository;
import com.pipewatch.domain.enterprise.repository.EnterpriseRepository;
import com.pipewatch.domain.user.model.entity.EmployeeInfo;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.EmployeeRepository;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pipewatch.global.statusCode.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EnterpriseServiceImpl implements EnterpriseService {
	private final UserRepository userRepository;
	private final EnterpriseRepository enterpriseRepository;
	private final EmployeeRepository employeeRepository;
	private final BuildingRepository buildingRepository;

	@Override
	public EnterpriseResponse.DetailDto getEnterpriseDetail(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		Enterprise enterprise;
		if (user.getRole() == Role.ENTERPRISE) {
			enterprise = enterpriseRepository.findByUserId(user.getId());
		} else {
			EmployeeInfo employeeInfo = employeeRepository.findByUserId(userId);
			enterprise = enterpriseRepository.findById(employeeInfo.getEnterprise().getId()).get();
		}

		if (enterprise == null) {
			throw new BaseException(ENTERPRISE_NOT_FOUND);
		}

		return EnterpriseResponse.DetailDto.toDto(enterprise);
	}

	@Override
	public EnterpriseResponse.ListDto getEnterpriseList() {
		List<Enterprise> enterprises = enterpriseRepository.findAll();

		List<EnterpriseResponse.EnterpriseDto> enterpriseDtos
				= enterprises.stream().map(EnterpriseResponse.EnterpriseDto::toDto).toList();

		return EnterpriseResponse.ListDto.builder()
				.enterprises(enterpriseDtos)
				.build();

	}

	@Override
	public EnterpriseResponse.BuildingListDto getBuildingList(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		if (user.getRole() == Role.USER) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		Long enterpriseId = null;
		if (user.getRole() == Role.ENTERPRISE) {
			enterpriseId = enterpriseRepository.findByUserId(user.getId()).getId();
		} else if (user.getRole() == Role.EMPLOYEE || user.getRole() == Role.ADMIN) {
			enterpriseId = employeeRepository.findByUserId(user.getId()).getEnterprise().getId();
		}

		List<String> buildings = buildingRepository.findDistinctNameByEnterpriseId(enterpriseId);

		return EnterpriseResponse.BuildingListDto.builder()
				.buildings(buildings)
				.build();
	}

	@Override
	public EnterpriseResponse.BuildingAndFloorListDto getBuildingAndFloorList(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		if (user.getRole() == Role.USER) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		Long enterpriseId = null;
		if (user.getRole() == Role.ENTERPRISE) {
			enterpriseId = enterpriseRepository.findByUserId(user.getId()).getId();
		} else if (user.getRole() == Role.EMPLOYEE || user.getRole() == Role.ADMIN) {
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
		List<EnterpriseResponse.BuildingAndFloorDto> buildingDtos = groupedFloors.entrySet().stream()
				.map(entry -> new EnterpriseResponse.BuildingAndFloorDto(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());

		return EnterpriseResponse.BuildingAndFloorListDto.builder()
				.buildings(buildingDtos)
				.build();
	}
}
