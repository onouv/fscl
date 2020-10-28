package fscl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;

import fscl.function.pageobjects.FunctionPage;

import javax.sql.DataSource;


public abstract class FsclEndToEndTest {
	
	
	private JdbcTemplate template;
	private PostgreSQLContainer<?> testDb = new PostgreSQLContainer<>("postgres:12.4")
			.withDatabaseName("fscl_functions");
	
	protected WebDriver driver; 
		
	protected void startEmptyDB(String databaseName) {
		
		this.testDb.start();		
		DataSource dataSource = DataSourceBuilder.create()
				.driverClassName("org.postgresql.Driver")
				.username(testDb.getUsername())
                .password(testDb.getPassword())
                .url(testDb.getJdbcUrl())
                .build();		
		this.template = new JdbcTemplate(dataSource);
		this.template.execute("DELETE FROM fscl_functions;");
	}
	
	protected void stopDB() {
		this.testDb.stop();
	}
	
	protected void setupSelenium() {
		System.setProperty(
				"webdriver.chrome.driver", 
				"/opt/selenium/chromedriver/chromedriver");
		this.driver = new ChromeDriver();
	}
	
	protected void tearDownSelenium() {
		this.driver.close();
	}
}
