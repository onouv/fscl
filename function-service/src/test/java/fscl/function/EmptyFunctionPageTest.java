package fscl.function;

import fscl.function.pageobjects.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;

public class EmptyFunctionPageTest {
	
	static final String PROJECT_NO = "20200902-001";
	
	@Nested
	public class GivenProjectWithoutFunctions {
		
		@Nested
		@TestInstance(Lifecycle.PER_CLASS)		
		public class WhenCallingFunctionPage {
			
			private WebDriver driver; 
			private FunctionPage page;
			
			@BeforeAll
			public void setup() {
				System.setProperty(
						"webdriver.chrome.driver", 
						"/opt/selenium/chromedriver/chromedriver");
				this.driver = new ChromeDriver();
				this.page = new FunctionPage(this.driver);
			}
			
			@AfterAll
			public void tearDown() {
				this.driver.close();
			}
			
			@Test
			public void thenShouldDisplayProjectNumber() {
				assertTrue(page.displaysProjectNo(PROJECT_NO));
			}
			
			@Test
			public void thenShouldShowNoFunctionsFoundBanner() {
				assertTrue(page.displaysNoFunctionsBanner());
			}
			
			@Test
			public void thenFunctionsNavigatorFieldShouldBeActive() {
				assertTrue(page.getFunctionsNavigationField().isActive());
			}
			
			@Test
			public void thenAllOtherNavigatorFieldsShouldBeAsExcpected() {
				assertTrue(page.getSystemsNavigationField().isDisabled());
				assertTrue(page.getComponentsNavigationField().isEnabled());
				assertTrue(page.getLocationsNavigationField().isDisabled());
			}
			
			@Test
			public void thenSaveButtonShouldBeDisabled() {
				assertFalse(page.saveButtonEnabled());
			}
			
			@Test 
			void thenDeleteButtonShouldBeDisabled() {
				assertFalse(page.deleteButtonEnabled());
			}
			
			@Test
			void thenNewButtonShouldBeEnabled() {
				assertTrue(page.newButtonEnabled());
			}
		}
	}
}
