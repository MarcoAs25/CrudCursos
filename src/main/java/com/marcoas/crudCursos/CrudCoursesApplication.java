package com.marcoas.crudCursos;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableCaching
public class CrudCoursesApplication {
	public static void main(String[] args) {
		SpringApplication.run(CrudCoursesApplication.class, args);
	}

	@Bean
	public OpenAPI myOpenAPI() {

		Contact contact = new Contact();
		contact.setEmail("marcoas2566@gmail.com");
		contact.setName("Marco Antônio da Silva");
		contact.setUrl("https://www.linkedin.com/in/marc025");

		License agplLicense = new License().name("AGPL V3").url("https://choosealicense.com/licenses/agpl-3.0/");

		Info info = new Info()
				.title("Crud Cursos")
				.version("1.0")
				.contact(contact)
				.description("Este é um crud simples utilizado principalmente como apoio a estudos de tecnologias complementares do spring boot")
				.license(agplLicense);

		return new OpenAPI().info(info);
	}
}
