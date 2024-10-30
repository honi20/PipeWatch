package com.pipewatch.domain.user.model.entity;

import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmployeeInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_info_id")
	private Long id;

	@NotNull
	private Long empNo;

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
