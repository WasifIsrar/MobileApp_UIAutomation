package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import basePage.WebBasePage;

public class DashboardLoginPage extends WebBasePage{
	
	private By emailField=By.id(":r1:");
	private By passwordField=By.id(":r3:");
	private By loginBtn=By.cssSelector("span[role='button']");
	
	public DashboardLoginPage(WebDriver driver) {
		super(driver);
	}

	public DashboardMainPage login() {
		waitForElementVisibility(emailField,10).sendKeys("wasif44@aioapp.com");
		driver.findElement(passwordField).sendKeys("Test@1234");
		driver.findElement(loginBtn).click();
		return new DashboardMainPage(driver);
	}
}
