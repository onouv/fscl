package fscl.function.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "test.webdriver")
public class FunctionTestConfig {
	
	private String name;
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String webdriverpath) {
		this.path = webdriverpath;
	}

	public String getName() {
		return name;
	}

	public void setName(String webdriver) {
		name = webdriver;
	}
	
	

}
