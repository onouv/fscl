package fscl.function.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.ArrayList;

public class FunctionPage {
	
	private WebDriver driver;
	
	private final static String URL = "http://localhost:3000/functions/20200902-001";	
	
	@FindBy(id="project-code")
	private WebElement projectNo;
	
	@FindBy(id="no-functions-found-alert")
	private WebElement noFunctionsFoundAlert;
	
	@FindBy(id="Save")
	private WebElement saveButton;
	
	@FindBy(id="Delete")
	private WebElement deleteButton;
	
	@FindBy(id="New")
	private WebElement newButton;
		
	private NavigationField functionsNavigationField;
	private NavigationField systemsNavigationField;
	private NavigationField componentsNavigationField;
	private NavigationField locationsNavigationField;
	
	private List<FunctionLine> lines = new ArrayList<>();
	
	public NavigationField getFunctionsNavigationField() {
		return functionsNavigationField;
	}
	
	public NavigationField getSystemsNavigationField() {
		return systemsNavigationField;
	}

	public NavigationField getComponentsNavigationField() {
		return componentsNavigationField;
	}

	public NavigationField getLocationsNavigationField() {
		return locationsNavigationField;
	}
	
	public FunctionPage(WebDriver driver) {
		this.driver = driver;
		this.driver.get(URL);
		this.functionsNavigationField = new NavigationField(driver, "FUNCTIONS");
		this.systemsNavigationField = new NavigationField(driver, "SYSTEMS");		
		this.componentsNavigationField = new NavigationField(driver, "COMPONENTS");
		this.locationsNavigationField = new NavigationField(driver, "LOCATIONS");
		
		PageFactory.initElements(driver, this);
	}
		
	public void ClickNew() throws IllegalStateException {
		
		this.newButton.click();
		
		FunctionLine newLine = new FunctionLine(this.driver, "=001");
		
		
	}
	
	public void ClickDelete() throws IllegalStateException {
		
	}
	
	public void ClickSave() throws IllegalStateException {
		
	}
	
	public boolean saveButtonEnabled() {
		return (this.saveButton.isEnabled() && this.saveButton.isDisplayed());		
	}
	
	public boolean deleteButtonEnabled() {
		return (this.deleteButton.isEnabled() && this.deleteButton.isDisplayed());
	}
	
	public boolean newButtonEnabled() {
		return (this.newButton.isEnabled() && this.newButton.isDisplayed());
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
}
