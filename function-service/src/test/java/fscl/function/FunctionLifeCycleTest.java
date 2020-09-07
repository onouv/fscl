package fscl.function;

import fscl.function.pageobjects.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;

public class FunctionLifeCycleTest {
	
	@Nested	
	public class GivenEmptyFunctionPage {
		
		private FunctionPage page;
		
		@Nested
		@TestInstance(Lifecycle.PER_CLASS)
		public class WhenClickingNewButton {			
						
			@BeforeAll
			public void setup() {
				WebDriver aut = new ChromeDriver();
				page = new FunctionPage(aut);
				page.ClickNew();
			}
			
			@Test
			public void thenShouldShowFunctionLine() {				
			}
			
			@Test 
			public void thenCodeFieldShouldShowValue() {
				final String INITIAL_FUNCTION_CODE = "=001";
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
