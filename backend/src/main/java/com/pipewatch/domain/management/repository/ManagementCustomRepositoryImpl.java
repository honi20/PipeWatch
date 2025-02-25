package com.pipewatch.domain.management.repository;

import com.pipewatch.domain.management.model.dto.ManagementResponse;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.State;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pipewatch.domain.user.model.entity.QEmployeeInfo.employeeInfo;
import static com.pipewatch.domain.user.model.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class ManagementCustomRepositoryImpl implements ManagementCustomRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<ManagementResponse.EmployeeDto> findPendingEmployeesOfEnterprise(Long enterpriseId) {
		return queryFactory
				.select(Projections.constructor(
						ManagementResponse.EmployeeDto.class,
						user.uuid,
						user.name,
						user.email,
						employeeInfo.empNo,
						employeeInfo.department,
						employeeInfo.empClass,
						Expressions.stringTemplate("CAST({0} AS string)", user.role)
				))
				.from(employeeInfo)
				.leftJoin(employeeInfo.user, user)
				.where(employeeInfo.enterprise.id.eq(enterpriseId))

				// 일반회원에서 사원으로 요청 중인 사원만
				.where(user.role.eq(Role.USER).and(user.state.eq(State.PENDING)))
				.fetch();
	}

	@Override
	public List<ManagementResponse.EmployeeDto> findEmployeesOfEnterprise(Long enterpriseId) {
		return queryFactory
				.select(Projections.constructor(
						ManagementResponse.EmployeeDto.class,
						user.uuid,
						user.name,
						user.email,
						employeeInfo.empNo,
						employeeInfo.department,
						employeeInfo.empClass,
						Expressions.stringTemplate("CAST({0} AS string)", user.role)
				))
				.from(employeeInfo)
				.join(employeeInfo.user, user)
				.where(
						employeeInfo.enterprise.id.eq(enterpriseId),
						user.state.eq(State.ACTIVE),
						user.role.ne(Role.USER)
				)
				.fetch();
	}

	@Override
	public List<ManagementResponse.EmployeeDto> findEmployeesOfEnterpriseByKeyword(Long enterpriseId, String keyword) {
		return queryFactory
				.select(Projections.constructor(
						ManagementResponse.EmployeeDto.class,
						user.uuid,
						user.name,
						user.email,
						employeeInfo.empNo,
						employeeInfo.department,
						employeeInfo.empClass,
						Expressions.stringTemplate("CAST({0} AS string)", user.role)
				))
				.from(employeeInfo)
				.join(employeeInfo.user, user)
				.where(
						employeeInfo.enterprise.id.eq(enterpriseId),
						user.state.eq(State.ACTIVE),
						user.role.ne(Role.USER)
								.and(
										user.name.containsIgnoreCase(keyword)
												.or(user.email.containsIgnoreCase(keyword))
								)
				)

				.fetch();
	}
}
