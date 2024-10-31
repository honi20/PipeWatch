package com.pipewatch.domain.enterprise.service;

import com.pipewatch.domain.enterprise.model.dto.EnterpriseResponse;
import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import com.pipewatch.domain.enterprise.repository.EnterpriseRepository;
import com.pipewatch.domain.user.model.entity.EmployeeInfo;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.EmployeeRepository;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import com.pipewatch.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import static com.pipewatch.global.statusCode.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EnterpriseServiceImpl implements EnterpriseService {
	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final EnterpriseRepository enterpriseRepository;
	private final EmployeeRepository employeeRepository;

	@Override
	public EnterpriseResponse.DetailDto getEnterpriseDetail(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		Enterprise enterprise;
		if (user.getRole() == Role.ROLE_ENTERPRISE) {
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
}
