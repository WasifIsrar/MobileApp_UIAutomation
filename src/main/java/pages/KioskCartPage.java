package pages;

import org.openqa.selenium.By;

import basePage.BasePage;
import io.appium.java_client.android.AndroidDriver;

public class KioskCartPage extends BasePage{
	
	private By backBtn=By.id("tvGoBack");
	private By emptyCartBtn=By.id("tvEmptyCart");

	public KioskCartPage(AndroidDriver driver) {
		super(driver);
	}
	
	public String getSubtotal() {
		return driver.findElement(kioskSubtotal).getText().substring(1);
	}
	
	public void goBack() {
		driver.findElement(backBtn).click();
	}
	
	public void emptyCart() {
		driver.findElement(emptyCartBtn).click();
	}
}
