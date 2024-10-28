package com.pipewatch.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
				.title("Pipe Watch API")
				.version("1.0")
				.description("Pipe Watch API Documentation");
		return new OpenAPI()
				.components(new Components())
				.info(info);
	}
}
