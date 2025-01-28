package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import basePage.BasePage;
import io.appium.java_client.android.AndroidDriver;
import utils.LogUtils;

public class MposPinPage extends BasePage{
	private By pinScreen=By.id("tvEnter");
	private By identificationNum=By.id("txtDeviceSerialNo");
	private By switchBtn=By.xpath("//android.widget.TextView[@text='Allow from this source']");
	private By backBtn=By.className("android.widget.Button");
	
	public MposPinPage(AndroidDriver driver) {
		super(driver);
	}
	
	public void allowApp() {
		waitForElementPresent(switchBtn,60).click();
		driver.findElement(backBtn).click();
	}
	
	public void checkForAllowApp() {
		try {
			 List<WebElement> switchButton = waitForElementsVisibility(switchBtn, 20); 
			    if (switchButton.size() > 0) {  
			        switchButton.get(0).click();  
			        driver.findElement(backBtn).click(); 
			    }
		}
		catch(Exception e)
		{
			
		}
	   
	}
	
	public MposTablePage enterPin(char[] pin) {
		getLogger().info("Waiting For Pin Screen");
		waitForElementPresent(pinScreen,120);
		
		getLogger().info("Entering Pin");
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+pin[0]+"']")).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+pin[1]+"']")).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+pin[2]+"']")).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+pin[3]+"']")).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+pin[4]+"']")).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+pin[5]+"']")).click();
		
		return new MposTablePage(driver);
	}
	
	public String onBoard() {
		WebElement identificationNumber=waitForElementPresent(identificationNum,30);
		return identificationNumber.getText().substring(22);
	}

}
