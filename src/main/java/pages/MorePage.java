package pages;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class MorePage extends BasePage{
	
	By moreScreen = By.xpath("//android.widget.TextView[@text='More']");
	By EmployeeManagementBtn = AppiumBy.accessibilityId("Employee management");
//	By myAccountBtn = AppiumBy.accessibilityId("My Account");
	By lostAndFound = AppiumBy.accessibilityId("Lost & found");
	
	public MorePage(AndroidDriver driver) {
		super(driver);
		
	}
	
    public boolean isScreenDisplayed(){
    	 return driver.findElement(moreScreen).isDisplayed();
     }

    public MomEmployeePage clickemployeeManagementBtn(){
    	 driver.findElement(EmployeeManagementBtn).click();
    	 return new MomEmployeePage(driver);
    }
    
    public MomLostAndFoundPage clicklostAndFoundBtn() {
	WebElement lostElement = waitForElementVisibility(lostAndFound, 20);
    lostElement.click();
	return new MomLostAndFoundPage(driver);
    }
   
    
	 

}
