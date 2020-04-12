package fscl.function.service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;


// Note annotations below to allow use of repositories and entities from fscl-core-lib
// since basePackages annotations have been used, we need to provide the necessary 
// packages from our jar as well. 
@SpringBootApplication
@EnableMongoRepositories(basePackages = {
	"fscl.core.db",							 
	"fscl.function.service.adapters.db"
})
@EntityScan(basePackages= {"fscl.core.domain"})
@ComponentScan(basePackages= {"fscl.core.domain","fscl.function.service"})
@EnableBinding(Source.class)
public class FunctionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunctionServiceApplication.class, args);
	}

}
