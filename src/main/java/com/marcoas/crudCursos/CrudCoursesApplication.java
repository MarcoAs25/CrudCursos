package com.marcoas.crudCursos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CrudCoursesApplication {
	public static void main(String[] args) {
		SpringApplication.run(CrudCoursesApplication.class, args);
	}
}
