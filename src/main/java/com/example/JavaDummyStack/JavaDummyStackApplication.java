package com.example.JavaDummyStack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.JavaDummyStack.mail.IMailSender;
import com.example.JavaDummyStack.user.IRoleDao;
import com.example.JavaDummyStack.user.Role;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;

@SpringBootApplication
public class JavaDummyStackApplication {

	@Autowired
	private IRoleDao roleDao;

	public static void main(String[] args) {
		SpringApplication.run(JavaDummyStackApplication.class, args);
	}

	@Bean
	public IMailSender mailSender() {
		return new com.example.JavaDummyStack.mail.MailSender();
	}

	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("SpringShop API").description("Spring shop sample application").version("v0.0.1")
						.license(new License().name("Apache 2.0").url("http://springdoc.org")))
				.externalDocs(new ExternalDocumentation().description("SpringShop Wiki Documentation")
						.url("https://springshop.wiki.github.org/docs"))
				.components(new Components().addSecuritySchemes("bearer-key",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
								.in(In.HEADER).name("Authorization")))
				.addSecurityItem(new SecurityRequirement().addList("bearer-key"));
	}

	@Bean
	CommandLineRunner commandLine() {
		return (args) -> {
			if (this.roleDao.count() == 0)
				addRole();
		};
	}

	private void addRole() {
		String roles[] = { "Admin", "User" };
		for (String role : roles) {
			Role roleObj = new Role();
			roleObj.setRole(role);
			roleObj.setEnabled(true);
			this.roleDao.save(roleObj);
		}
	}

}
