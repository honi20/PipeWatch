package com.pipewatch.domain.user.model.entity;

import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmployeeInfo {
	@Id
	@GeneratedValue
	@Column(name = "employee_info_id")
	private Long id;

	@NotNull
	private Integer empNo;

	@NotNull
	private String department;

	@NotNull
	private String empClass;

	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "enterprise_id")
	private Enterprise enterprise;
}
