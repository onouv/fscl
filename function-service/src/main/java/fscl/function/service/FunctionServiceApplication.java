package fscl.function.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


// Note annotations below to allow use of repositories and entities from fscl-core-lib
// since basePackages annotations have been used, we need to provide the necessary 
// packages from our jar as well. 
@SpringBootApplication
@EnableJpaRepositories(basePackages = {
		"fscl.core.db",
		"fscl.function.service.adapters.db"
})
//@EntityScan(basePackages= {"fscl.core.domain"})
@EntityScan(basePackages= {
		"fscl.core.domain",
		"fscl.component.api",
		"fscl.project",
		"fscl.function.service.domain"
})
@ComponentScan(basePackages= {
		"fscl.core.domain",
		"fscl.function.service",
		"fscl.component.api"
})
@EnableBinding(Source.class)
public class FunctionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunctionServiceApplication.class, args);
	}

}
