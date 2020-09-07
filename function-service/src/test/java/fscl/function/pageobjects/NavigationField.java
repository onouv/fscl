package fscl.function.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class NavigationField {
	
	private WebDriver driver;
	private WebElement field;
	
	public NavigationField(WebDriver driver, String cssType) {
		this.driver = driver;
		String css = "a[data-rb-event-key='" + cssType + "']";
		this.field = this.driver.findElement(By.cssSelector(css));
	}
	
	public boolean isActive() {
		String className = this.field.getAttribute("class");
		return className.equals("nav-link active");
	}	
	
	public boolean isEnabled() {
		String className = this.field.getAttribute("class");
		
		return className.equals("nav-link");		
	}
	
	public boolean isDisabled() {
		String className = this.field.getAttribute("class");
		return className.equals("nav-link disabled");
	}

}
