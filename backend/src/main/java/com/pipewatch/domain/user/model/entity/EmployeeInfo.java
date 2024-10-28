package com.pipewatch.domain.user.model.entity;

import jakarta.persistence.*;
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

	private Integer empNo;

	private String department;

	private String empClass;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
}
