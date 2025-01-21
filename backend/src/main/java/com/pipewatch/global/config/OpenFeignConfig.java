package com.pipewatch.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.pipewatch.domain.pipelineModel.service")
public class OpenFeignConfig {
}
