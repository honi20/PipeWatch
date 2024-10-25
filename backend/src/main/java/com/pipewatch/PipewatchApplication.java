package com.pipewatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * [TODO]
 * DB 설정 후 exclude 설정 제거
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class PipewatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(PipewatchApplication.class, args);
	}

}
