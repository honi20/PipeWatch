package com.pipewatch.domain.user.repository;

import com.pipewatch.domain.user.model.dto.UserResponse;
import com.pipewatch.domain.user.model.entity.Role;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.pipewatch.domain.enterprise.model.entity.QEnterprise.enterprise;
import static com.pipewatch.domain.user.model.entity.QEmployeeInfo.employeeInfo;
import static com.pipewatch.domain.user.model.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public UserResponse.MyPageDto findUserDetailByUserId(Long userId) {
		return queryFactory
				.select(Projections.constructor(
						UserResponse.MyPageDto.class,
						user.name,
						user.email,
						Expressions.stringTemplate("CAST({0} AS string)", user.role),
						Expressions.stringTemplate("CAST({0} AS string)", user.state),
						getEnterpriseNameExpression(userId),
						Projections.constructor(
								UserResponse.EmployeeDto.class,
								employeeInfo.empNo,
								employeeInfo.department,
								employeeInfo.empClass
						).skipNulls()
				))
				.from(user)
				.leftJoin(employeeInfo).on(user.id.eq(employeeInfo.user.id))
				.where(user.id.eq(userId))
				.fetchOne();
	}

	private Expression<String> getEnterpriseNameExpression(Long userId) {
		return Expressions.cases()
				.when(user.role.eq(Role.ENTERPRISE))
				.then(user.name)
				.when(user.role.eq(Role.ADMIN).or(user.role.eq(Role.EMPLOYEE)))
				.then(employeeInfo.enterprise.name)
				.otherwise((String) null);
	}
}