package com.pipewatch.domain.enterprise.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EnterpriseResponseDto {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EnterpriseDetailResponseDto {
		private String name;
		private String industry;
		private String managerEmail;
		private String managerPhoneNumber;
	}
}
