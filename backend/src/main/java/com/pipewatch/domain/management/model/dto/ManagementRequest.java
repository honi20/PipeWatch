package com.pipewatch.domain.management.model.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagementRequest {
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AccessModifyDto {
		private String userUuid;
		private String newRole;
	}
}
