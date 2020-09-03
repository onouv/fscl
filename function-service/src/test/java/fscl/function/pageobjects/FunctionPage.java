package fscl.function.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FunctionPage {
	
	private WebDriver driver;
	
	private final static String URL = "http://localhost:8081/functions";
	
	public FunctionPage(WebDriver driver) {
		this.driver = driver;
		this.driver.get(URL);
		PageFactory.initElements(driver, this);
	}
	
	public void ClickNew() throws IllegalStateException {
		
	}
	
	public void ClickDelete() throws IllegalStateException {
		
	}
	
	public void ClickSave() throws IllegalStateException {
		
	}
	
	public boolean saveButtonEnabled() {
		return false;
	}
	
	public boolean deleteButtonEnabled() {
		return false;
	}
	
	public boolean newButtonEnabled() {
		return false;
	}
	
	public boolean displaysProjectNo(String projectNo) {
		return false;
	}
	
	public boolean displaysNoFunctionsBanner() {
		return false;
	}
	
	public boolean functionsNavigatorFieldEnabled() {
		return false;
	}
	
	public boolean systemsNavigatorFieldEnabled() {
		return false;
	} 

	public boolean componentsNavigatorFieldEnabled() {
		return false;
	}
	
	public boolean locationsNavigatorFieldEnabled() {
		return false;
	}
}
