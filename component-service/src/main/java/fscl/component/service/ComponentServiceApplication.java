package fscl.component.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


//Note annotations below to allow use of repositories and entities from fscl-core-lib
//since basePackages annotations have been used, we need to provide the necessary 
//packages from our jar as well. 

@SpringBootApplication
@EnableMongoRepositories(basePackages = {
	"fscl.core.db",							 
	"fscl.component.service.adapters"//.db"
})
@EntityScan(basePackages= {"fscl.core.domain"})
@ComponentScan(basePackages= {"fscl.core.domain","fscl.component.service"})
@EnableBinding(Source.class)
public class ComponentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComponentServiceApplication.class, args);
	}

}
