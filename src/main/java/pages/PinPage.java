package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import basePage.BasePage;
import io.appium.java_client.android.AndroidDriver;

public class PinPage extends BasePage{
	private By pinScreen=By.id("tvEnter");
	private By identificationNum=By.id("txtDeviceSerialNo");
	
	public PinPage(AndroidDriver driver) {
		super(driver);
	}
	
	//Enters Pin and returns TablePage object
	public TablePage enterPin(char[] pin) {
		getLogger().info("Waiting For Pin Screen");
		waitForElementPresent(pinScreen,120);
		getLogger().info("Entering Pin");
		driver.findElement(By.xpath("//android.widget.Button[@text='"+pin[0]+"']")).click();
		driver.findElement(By.xpath("//android.widget.Button[@text='"+pin[1]+"']")).click();
		driver.findElement(By.xpath("//android.widget.Button[@text='"+pin[2]+"']")).click();
		driver.findElement(By.xpath("//android.widget.Button[@text='"+pin[3]+"']")).click();
		driver.findElement(By.xpath("//android.widget.Button[@text='"+pin[4]+"']")).click();
		driver.findElement(By.xpath("//android.widget.Button[@text='"+pin[5]+"']")).click();
		return new TablePage(driver);
	}
	
	public String onBoard() {
		WebElement identificationNumber=waitForElementPresent(identificationNum,30);
		return identificationNumber.getText().substring(22);
	}
}
