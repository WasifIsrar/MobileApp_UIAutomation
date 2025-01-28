package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;
import io.appium.java_client.android.AndroidDriver;
import utils.LogUtils;

public class CashDrawerPage extends BasePage{
	
	private By thisStationDrawer=By.id("clStation");
	private By totalBalance=By.id("tvTotalBalance");
	private By cashInBtn=By.id("btCashIn");
	private By reasonDropdown=By.id("clCashInReason");
	private By reOpenBtn=By.xpath("//android.widget.TextView[@text='Re-open Drawer']");
	private By closeDrawertext=By.xpath("//android.widget.TextView[@text='Close Drawer']");
	private By closeDrawerBtn=By.id("tvCloseDrawer");
	private By expectedCashLabel=By.id("tvExpectedCashValue");
	private By submitBtn=By.id("btSubmit");
	private By thisStation=By.id("tvThisStation");
	private By action=By.xpath("(//android.widget.TextView[contains(@resource-id,'tvAction')])[2]");
	private By balanceAfter=By.id("tvBalanceAfter");
	private By enteredAmount=By.id("paymentAmount_ET");
	private By reasonComments=By.id("tvReasonComments");
	private By adjustBalanceBtn=By.id("rlAdjustBalance");
	private By loggedAmount=By.id("tvAmount");
	private By houseDrawer=By.id("clHouseDrawer");
	private By amount=By.id("tvAmount");
	private By otherStation =By.id("tvOtherStation");

	
	public CashDrawerPage(AndroidDriver driver) {
		super(driver);
	}
	
	/*
	 * Opens this station drawer
	 */
	public int openDrawer(String drawer) {
		getLogger().info("Going to this station's Drawer");
		if(drawer.equals("This Station")) {
			waitForElementVisibility(thisStationDrawer,5).click();
		}
		else if(drawer.equals("House")) {
			driver.findElement(houseDrawer).click();
		}
		
		getLogger().info("Checking if Drawer is Closed");
		List<WebElement> status=driver.findElements(reOpenBtn);
		
		return status.size();
	}
	
	public void reopenDrawer() {
		getLogger().info("Opening Drawer");
		driver.findElement(reOpenBtn).click();
		waitForElementVisibility(closeDrawertext,5);
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.textToBe(action, "Open Drawer"));
	}
	public double getTotalBalance() {
		return Double.parseDouble(driver.findElement(totalBalance).getText().substring(1));
	}
	
	/*
	 * Goes back to table layout
	 */
	public void goBacktoTableServe() {
		getLogger().info("Going back to Table Layout");
		driver.findElement(backBtn).click();
		waitForElementPresent(otherStation,5);
		driver.findElement(backBtn).click();
		
	}

	public void verifyCashIn() {
		getLogger().info("Performing Cash In");
		driver.findElement(cashInBtn).click();
		
		driver.findElement(reasonDropdown).click();
		tapByCoordinates(1126,400);
		driver.findElement(btn1).click();
		WebElement numpadBtn0=driver.findElement(btn0);
		for(int i=0;i<3;i++) {
			numpadBtn0.click();
		}
		driver.findElement(numpadDoneBtn).click();
		
	}
	
	/*
	 * Clicks Close Drawer button until Next Screen displayed
	 * Enters the amount received as argument as closed cash
	 * Returns Entered Amount
	 */
	public String verifyDrawerClosed(String amount) {
		boolean isClicked=true;
		WebElement closeBtn=driver.findElement(closeDrawerBtn);
		List<WebElement> expectedCashElement;
		getLogger().info("Waiting For expected Cash Screen to Display");
		while(isClicked) {
			closeBtn.click();
			expectedCashElement=driver.findElements(expectedCashLabel);
			if(expectedCashElement.size()>0) {
				isClicked=false;
			}
		}
		
		if(amount.contains(".")) {
			String[] parts=amount.split("\\.");
			String dollars=parts[0];
			String cents=parts[1];
			if(cents.length()==1) {
				cents=cents+"0";
			}
			amount=dollars+cents;
		}
		else {
			amount=amount+"00";
		}
		getLogger().info("Entering Cash");
		char[] amountDigits=amount.toCharArray();
		for(char amountDigit:amountDigits) {
			driver.findElement(By.xpath("//android.widget.TextView[@text='"+amountDigit+"']")).click();
		}
		String amountEntered=driver.findElement(enteredAmount).getText();
		driver.findElement(numpadDoneBtn).click();
		driver.findElement(submitBtn).click();
		getLogger().info("Cash Entered");
		
		return amountEntered;
	}
	
	public String getLog() {
		return waitForElementVisibility(action,20).getText();
	}
	
	public String getBalanceAfter() {
		return driver.findElements(balanceAfter).get(1).getText().substring(1);
	}
	
	public String getComments() {
		return driver.findElements(reasonComments).get(1).getText();
	}
	
	
	/*
	 * Adjusts starting balance as 10 dollars
	 */
	public void adjust() {
		getLogger().info("Adjusting Balance");
		driver.findElement(adjustBalanceBtn).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='1']")).click();
		WebElement zeroBtn=driver.findElement(By.xpath("//android.widget.TextView[@text='0']"));
		for(int i=0;i<3;i++) {
			zeroBtn.click();
		}
		driver.findElement(numpadDoneBtn).click();
		
		getLogger().info("Balance Entered");
	}
	
	public String expectedCashLabel() {
		return driver.findElement(expectedCashLabel).getText().substring(1);
	}
	
	public String getLoggedAmount() {
		return driver.findElements(loggedAmount).get(1).getText().substring(1);
	}
	
	public void goBack() {
		driver.findElement(backBtn).click();
	}
	public void isCashLogsPageDisplayed() {
		waitForElementVisibility(cashLogs,5);
	}
	public String getAmountofTicket() {
		List <WebElement> element =waitForElementsVisibility(amount,10);
		return element.get(1).getText().substring(1);
	}
}
