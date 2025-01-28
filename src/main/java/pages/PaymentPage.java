package pages;

import org.openqa.selenium.By;

import basePage.BasePage;
import io.appium.java_client.android.AndroidDriver;
import utils.LogUtils;

public class PaymentPage extends BasePage{
	
	private By cashTendered=By.id("cashTenderedET");
	private By skipBtn=By.id("btnSkip");
	private By paymentDoneStatus=By.id("paymentDone");
	private By addTipLater = By.id("btnTip");
	
	
	public PaymentPage(AndroidDriver driver) {
		super(driver);
	}
	
	public void selectAmount() {
		getLogger().info("Selecting exact amount");
		driver.findElement(exactAmount).click();
		driver.findElement(numpadDoneBtn).click();
		
	}
	
	public void noReceipt() {
		getLogger().info("Selecting no Receipt Option");
		driver.findElement(noReceiptBtn).click();
		
		getLogger().info("Waiting For Payment to Complete");
	}
	
	public String getCashTendered() {
		return driver.findElement(cashTendered).getText().substring(1);
	}
	
	public void skipTip() {
		waitForElementVisibility(skipBtn, 5).click();
	}
	
	public void addTipLater()
	{
		driver.findElement(addTipLater).click();
	}
	

	public void waitForNoReceipt() {
		waitForElementPresent(noReceiptBtn,5).click();
	}
	
	public void waitForPaymentComplete() {
		waitForElementPresent(paymentDoneStatus,60);
	}
}
