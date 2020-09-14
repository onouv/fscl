package fscl.core.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LinkButton {
	
	WebDriver driver;
	WebElement button;
	String code;
	
	public LinkButton(WebDriver driver, String code) {
		this.driver = driver;
		this.code = code;
		this.button = this.driver.findElement(By.id(code));
	}
	
	public void click() throws IllegalStateException {
		if(this.button.isEnabled()) {
			this.button.click();
		} else {
			throw new IllegalStateException("Button " + this.code + "not enabled");
		}
	}
	
	public boolean isEnabled() {
		return this.button.isEnabled();
	}
	
	public boolean isDisplayed() {
		return this.button.isDisplayed();
	}
	
	public boolean isActive() {
		String className = this.button.getAttribute("class");
		return className.contains("btn-primary");
	}
}
