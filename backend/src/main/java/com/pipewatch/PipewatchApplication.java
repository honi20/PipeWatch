package com.pipewatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class PipewatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(PipewatchApplication.class, args);
	}

}
