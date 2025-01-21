package com.pipewatch.domain.management.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
		@Schema(description = "직원 유저의 UUID", example = "1604b772-adc0-4212-8a90-81186c57f601")
		private String userUuid;
		@Schema(description = "새로운 Role (EMPLOYEE/ADMIN)", example = "ADMIN")
		private String newRole;
	}
}
