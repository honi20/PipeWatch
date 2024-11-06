package com.pipewatch.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
		servers = {
				@Server(url = "http://localhost:8080", description = "local"),
				@Server(url = "https://api.pipewatch.co.kr", description = "deploy")
		}
)
@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		String jwt = "JWT";
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
		Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
				.name(jwt)
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT")
		);
		return new OpenAPI()
				.components(new Components())
				.info(apiInfo())
				.addSecurityItem(securityRequirement)
				.components(components);
	}

	private Info apiInfo() {
		return new Info()
				.title("API Test") // API의 제목
				.description("Let's practice Swagger UI") // API에 대한 설명
				.version("1.0.0"); // API의 버전
	}
}
