package pages;

import org.openqa.selenium.By;

import basePage.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class KioskLandingPage extends BasePage{
	
	private By continueAsGuestBtn=By.id("tvContinue");
	private By allowSwitch=By.id("android:id/switch_widget");
	private By backBtn=AppiumBy.accessibilityId("Navigate up");
	private By firstScreen=By.id("vpBanner");
	private By exitFullScreenBtn=By.id("android:id/ok");

	public KioskLandingPage(AndroidDriver driver) {
		super(driver);
	}
	
	public void exitFullScreen() {
		driver.findElement(exitFullScreenBtn).click();
	}
	
	public void tapFirstScreen() {
		driver.findElement(firstScreen).click();
	}

	public KioskMenuPage continueAsGuest() {
		driver.findElement(continueAsGuestBtn).click();
		return new KioskMenuPage(driver);
	}
	
	public void allowApp() {
		waitForElementVisibility(allowSwitch,5).click();
		driver.findElement(backBtn).click();
	}

}
