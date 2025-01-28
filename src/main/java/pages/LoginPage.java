package pages;

import basePage.BasePage;
import io.appium.java_client.android.AndroidDriver;

public class LoginPage extends BasePage{
	
	public LoginPage(AndroidDriver driver) {
		super(driver);		
	}

	public MomDashboardPage verifyLogin(String emailInput,String passwordInput)
	{
		driver.findElement(emailField).sendKeys(emailInput);
		waitForElementToBeClickable(nextButton,5);
		driver.findElement(nextButton).click();
		waitForElementPresent(passwordField,15).sendKeys(passwordInput);
		waitForElementToBeClickable(loginButtonAgain,5);
		driver.findElement(loginButtonAgain).click();
		return new MomDashboardPage(driver);
	}
}