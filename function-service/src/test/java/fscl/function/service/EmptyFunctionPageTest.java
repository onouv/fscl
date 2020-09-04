package fscl.function.service;

import fscl.function.pageobjects.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.openqa.selenium.WebDriver;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableConfigurationProperties(value = FunctionTestConfig.class)
@TestPropertySource("test.properties")
public class EmptyFunctionPageTest {
	
	@Autowired
	FunctionTestConfig config;
	
	@Nested
	public class GivenProjectWithoutFunctions {
		
		
		static final String PROJECT_NO = "20200101-001";
		
		@Nested
		@TestInstance(Lifecycle.PER_CLASS)		
		public class WhenCallingFunctionPage {
			
			@Autowired
			FunctionTestConfig config;
			
			private FunctionPage page;
			
			@BeforeAll
			public void setup() {
				System.setProperty(
						config.getName(), // "webdriver.chrome.driver" 
						config.getPath());// "/opt/selenium/chromedriver/chromedriver"
				WebDriver aut = new ChromeDriver();
				this.page = new FunctionPage(aut);
				
			}
			
			@Test
			public void thenShouldDisplayProjectNumber() {
				assertTrue(this.page.displaysProjectNo(PROJECT_NO));
			}
			
			@Test
			public void thenShouldShowNoFunctionsFoundBanner() {
				assertTrue(this.page.displaysNoFunctionsBanner());
			}
			
			@Test
			public void thenFunctionsNavigatorFieldShouldBeEnabled() {
				
			}
			
			@Test
			public void thenAllOtherNavigatorFieldsShouldBeDisabled() {
				assertFalse(this.page.systemsNavigatorFieldEnabled());
				assertFalse(this.page.componentsNavigatorFieldEnabled());
				assertFalse(this.page.locationsNavigatorFieldEnabled());
			}
			
			@Test
			public void thenSaveButtonShouldBeDisabled() {
				assertFalse(this.page.saveButtonEnabled());
			}
			
			@Test 
			void thenDeleteButtonShouldBeDisabled() {
				assertFalse(this.page.deleteButtonEnabled());
			}
			
			@Test
			void thenNewButtonShouldBeEnabled() {
				assertTrue(this.page.newButtonEnabled());
			}
		}
	}
}
