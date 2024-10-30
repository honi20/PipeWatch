package com.pipewatch.domain.enterprise.model.dto;

import com.pipewatch.domain.enterprise.model.entity.Enterprise;
import lombok.*;

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

		public static DetailDto toDto(Enterprise enterprise) {
			return DetailDto.builder()
					.name(enterprise.getName())
					.industry(enterprise.getIndustry())
					.managerEmail(enterprise.getManagerEmail())
					.managerPhoneNumber(enterprise.getManagerPhoneNumber())
					.build();
		}
	}
}
