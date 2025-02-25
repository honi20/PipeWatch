package com.pipewatch.domain.enterprise.model.dto;

import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EnterpriseResponse {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DetailDto {
		private String name;
		private String industry;
		private String managerEmail;
		private String managerPhoneNumber;

		public static DetailDto fromEntity(Enterprise enterprise) {
			return DetailDto.builder()
					.name(enterprise.getName())
					.industry(enterprise.getIndustry())
					.managerEmail(enterprise.getManagerEmail())
					.managerPhoneNumber(enterprise.getManagerPhoneNumber())
					.build();
		}
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ListDto {
		private List<EnterpriseDto> enterprises;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class EnterpriseDto {
		private Long enterpriseId;
		private String name;
		private String industry;

		public static EnterpriseDto fromEntity(Enterprise enterprise) {
			return EnterpriseDto.builder()
					.enterpriseId(enterprise.getId())
					.name(enterprise.getName())
					.industry(enterprise.getIndustry())
					.build();
		}
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BuildingListDto {
		private List<String> buildings;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BuildingAndFloorListDto {
		private List<BuildingAndFloorDto> buildings;
	}

	@Getter
	@AllArgsConstructor
	public static class BuildingAndFloorDto {
		private String building;
		private List<Integer> floors;
	}
}
