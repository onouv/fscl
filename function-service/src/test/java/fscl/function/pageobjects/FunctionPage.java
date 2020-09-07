package fscl.function.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FunctionPage {
	
	private WebDriver driver;
	
	private final static String URL = "http://localhost:3000/functions/20200902-001";
	
	@FindBy(id="project-code")
	private WebElement projectNo;
	
	@FindBy(id = "no-functions-found-alert")
	private WebElement noFunctionsFoundAlert;
	
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
		String text = this.projectNo.getText();
		String[] splits = text.split("Project: ");
		return (splits[0].equals("") && splits[1].equals(projectNo));
	}
	
	public boolean displaysNoFunctionsBanner() {
		String actualText = this.noFunctionsFoundAlert.getText();
		String expectedText = "No functions found";
		return actualText.equals(expectedText);
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
