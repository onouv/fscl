package fscl.function.pageobjects;

import fscl.core.pageobjects.LinkButton;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FunctionLine {
	
	private WebDriver driver;
	
	private LinkButton FButton;
	private LinkButton SButton;
	private LinkButton CButton;
	private LinkButton LButton;
	
	/*
	private WebElement ChangedFlag;
	private WebElement MarkedFlag;
	
	private WebElement CodeField;
	private WebElement NameField;
	private WebElement DescriptionField;
	*/
	
	public FunctionLine(WebDriver driver, String code) {
		
		this.driver = driver;
		this.FButton = new LinkButton(this.driver, "F" + code);
		this.SButton = new LinkButton(this.driver, "S" + code);
		this.CButton = new LinkButton(this.driver, "C" + code);
		this.LButton = new LinkButton(this.driver, "L" + code);	
		
	}
	
	public LinkButton getFButton() {
		return FButton;
	}

	public LinkButton getSButton() {
		return SButton;
	}

	public LinkButton getCButton() {
		return CButton;
	}

	public LinkButton getLButton() {
		return LButton;
	}
}
