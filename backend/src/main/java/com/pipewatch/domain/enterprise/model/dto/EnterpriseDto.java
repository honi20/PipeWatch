package com.pipewatch.domain.enterprise.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseDto {
    private Long enterpriseId;
    private String name;
    private String industry;
}
