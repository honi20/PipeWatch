package com.pipewatch.domain.enterprise.model.dto;

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
	}
}
