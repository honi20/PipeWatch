package com.pipewatch.domain.management.model.dto;

import com.pipewatch.domain.user.model.entity.EmployeeInfo;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagementResponse {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EmployeeWaitingListDto {
		List<EmployeeDto> employees;

	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EmployeeListDto {
		private List<EmployeeDto> employees;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EmployeeSearchDto {
		private List<EmployeeDto> employees;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BuildingListDto {
		private List<BuildingDto> buildings;
	}

	@Getter
	public static class EmployeeDto {
		private String uuid;
		private String name;
		private String email;
		private Long empNo;
		private String department;
		private String empClass;
		private String role;

		public EmployeeDto(String uuid, String name, String email, Long empNo, String department, String empClass, String role) {
			this.uuid = uuid;
			this.name = name;
			this.email = email;
			this.empNo = empNo;
			this.department = department;
			this.empClass = empClass;
			this.role = role;
		}
	}

	@Getter
	@AllArgsConstructor
	public static class BuildingDto {
		private String building;
		private List<Integer> floors;
	}
}
