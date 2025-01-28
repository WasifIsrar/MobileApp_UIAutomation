package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.functions.ExpectedCondition;

public class MomDashboardPage extends BasePage {
	
	By Dashboardbtn = AppiumBy.accessibilityId("Dashboard");
	By profileIcon = AppiumBy.accessibilityId("Manager");
	By myAccountBtn = AppiumBy.accessibilityId("My Account");
	
	
	By MoreBtn = AppiumBy.accessibilityId("More");
	By clickPassword =AppiumBy.accessibilityId("Changed Password");
	By netSales =By.xpath("//android.widget.TextView[@text=\'Net sales\']/following-sibling::android.widget.TextView");
	
	public MomDashboardPage(AndroidDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}



	public boolean isScreenDisplay(){
		return driver.findElement(Dashboardbtn).isDisplayed();
		
	}
	
	public void netSalesDisplayed() {
		new WebDriverWait(driver,Duration.ofSeconds(5)).until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
				WebElement sales=driver.findElement(netSales);
				String amount=sales.getText();
				return !amount.isEmpty() ;
				}
		});
	}
	public MyAccountPage clickAccBtn() {
		driver.findElement(profileIcon).click();
		driver.findElement(myAccountBtn).click();
		return new MyAccountPage(driver);
	}
	public void enterPassword() {
		driver.findElement(clickPassword).click();
	}
	
	public MorePage clickMoreBtn() {
		driver.findElement(MoreBtn).click();
		return new MorePage(driver);
	}
	
	
}
