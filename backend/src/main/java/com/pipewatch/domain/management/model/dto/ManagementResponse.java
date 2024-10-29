package com.pipewatch.domain.management.model.dto;

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
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EmployeeDto {
		private String name;
		private String email;
		private Long empNo;
		private String department;
		private String empClass;
		private String role;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BuildingDto {
		private String building;
		private List<Integer> floors;
	}
}
