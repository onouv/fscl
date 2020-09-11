package fscl.function;

import fscl.function.pageobjects.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;

public class FunctionLifeCycleTest {
	
	@Nested	
	public class GivenEmptyFunctionPage {
		
		private FunctionPage page;		
		private WebDriver driver;
		
		@Nested
		@TestInstance(Lifecycle.PER_CLASS)
		public class WhenClickingNewButton {
			
			final String INITIAL_FUNCTION_CODE = "=001";
			
			protected WebElement waitForElementWith(String id) {
				By element = By.id(id);
				WebDriverWait wait = new WebDriverWait(driver, 5);
				wait.until(ExpectedConditions.presenceOfElementLocated(element));
				return driver.findElement(element);				
			}
						
			@BeforeAll
			public void setup() {
				driver = new ChromeDriver();				
				page = new FunctionPage(driver);				
				page.ClickNew();				
			}			
			
			@Test
			public void thenShouldShowFunctionLine() {
				String id = "function-line" + INITIAL_FUNCTION_CODE;
				this.waitForElementWith(id);
			}
						
			@Test 
			public void thenCodeFieldShouldShowValue() {
				String id = INITIAL_FUNCTION_CODE + "#code";
				WebElement codeField = this.waitForElementWith(id);
				String code = codeField.getAttribute("value");
				assertEquals(INITIAL_FUNCTION_CODE, code);	
			}
			
			@Test 
			public void thenOnly_C_ButtonShouldBeEnabled() {
				
			}
			
			@Test
			public void thenNameFieldShouldBeEmpty() {
				
			}
			
			@Test
			public void thenDescriptionFieldShouldBeEmpty() {
				
			}
			
			@Test 
			public void thenChangedFlagShouldShow_C() {
				
			}
			
			@Test 
			public void thenMarkedFlagShouldBeChecked() {
				
			}
			
			@Test
			public void thenSaveButtonShouldBeEnabled() {
				
			}
			
			@Test
			public void thenDeleteButtonShouldBeEnabled() {
				
			}
			
			@Test
			public void thenNewButtonShouldBeEnabled() {
				
			}
		}
	}
	
	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	public class GivenFunctions {
		
		@Nested
		public class WhenSelectingOneFunction {
			
		}
	}			
}
