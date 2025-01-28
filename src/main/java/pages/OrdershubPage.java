package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import utils.LogUtils;

public class OrdershubPage extends BasePage{
	
	private By gotoPaymentBtn=By.id("goToPaymentBtn");
	private By refundBtn=By.id("refundBtn");
	private By selectAllBtn=By.id("selectAllBtn");
	private By itemsNextBtn=By.id("itemsNextBtn");
	private By reasonNextBtn=By.id("reasonNextBtn");
	private By confirmBtn=By.id("confirmBtn");
	private By refundSuccess=By.xpath("//android.widget.TextView[@text='Refund Successful!']");
	private By refundAmount=By.id("refundAmount");
	private By billStatus=By.id("billStatusTV");
	private By completedTab=By.id("tvCompleted");
	private By subTotalAmount=By.id("subTotalET");
	private By tax=By.id("taxET");
	private By serviceCharge=By.id("scET");
	private By totalAmount=By.id("totalET");
	private By balanceDue=By.id("balanceDueET");
	private By refundPrice=By.id("rTotal");
	private By paymentScreen=By.id("ticket1TV");
	private By refundAmountTab=AppiumBy.accessibilityId("Refund Amount");
	private By refundAmountField=By.id("rAmount_ET");
	private By backspaceBtn=By.id("btn_backspace");
	private By paymentNextBtn=By.id("paymentNextBtn");
	private By totalRefundAmount=By.id("rTotal");
	private By refundAmountCheckbox=By.id("rAmount_Checkbox");
	private By taxCheckbox=By.id("rTax_Checkbox");
	private By ordershubScreen=By.id("ordersHubTicketFragment");
	private By itemName=By.id("itemName");
	private By tableNum=By.id("tvTable");
	private By order=By.id("llItem");
	private By source=By.id("tvSource");
	private By ticketNum = By.id("tvTicketNum");
	private By unclosed = By.id("tvUnclosed");
	private By addATip =By.id("addATip");
	private By tipAmount=By.id("tipAmount");
	private By btnOne= By.id("btn_1");
	private By btnTwo= By.id("btn_0");
	private By btnDone= By.id("btn_done");
	

	public OrdershubPage(AndroidDriver driver) {
		super(driver);
	}
	
	public boolean isPageDisplayed() {
		return waitForElementVisibility(ordershubScreen,5).isDisplayed();
	}
	
	public String getStatus(String id) {
		return waitForElementVisibility(By.xpath("//android.widget.TextView[@text='"+id+"']/following-sibling::android.widget.RelativeLayout/android.widget.TextView[contains(@resource-id,'tvPayment')]"),5).getText();
	}
	
	public void gotoCompleted() {
		driver.findElement(completedTab).click();
	}
	
	public void openEntry(String id) {
		getLogger().info("Opening Entry");
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+id+"']")).click();
		
	}
	public void addATipBtn() {
		waitForElementVisibility(addATip, 10);
		driver.findElement(addATip).click();
	}
	public void enteringTip() {
		waitForElementVisibility(tipAmount, 10);
		driver.findElement(btnOne).click();
		for (int i = 0; i < 3; i++) {
	        driver.findElement(btnTwo).click();
	    }
		driver.findElement(btnDone).click();
	}
	public void waitForInvisibility() {
		waitForElementInvisibility(tipAmount, 20);
	}
	
	public void gotoPayment() {
		getLogger().info("Going to Payment");
		waitForElementVisibility(gotoPaymentBtn, 10).click();
		
		getLogger().info("Waiting For Payment Screen to display");
		waitForElementPresent(paymentScreen,5);
	}
	
	public void gotoRefund() {
		getLogger().info("Navigating to Refund");
		driver.findElement(refundBtn).click();
		
	}
	
	public void refundItem() {
		driver.findElement(selectAllBtn).click();
		driver.findElement(itemsNextBtn).click();
		
	}
	
	public boolean selectReasonAndConfirm() {
		getLogger().info("Selecting Reason");
		driver.findElement(reasonNextBtn).click();
		
		driver.findElement(confirmBtn).click();
		
		getLogger().info("Waiting For Success Message");
		return waitForElementPresent(refundSuccess,5).isDisplayed();
	}
	
	public void noReceipt() {
		getLogger().info("Selecting no Receipt Option");
		waitForElementVisibility(noReceiptBtn,5).click();
		
	}
	
	/*
	 * Checks for refund status
	 */
	public boolean isTextDisplayed() {
		getLogger().info("Checking for Refund Status");
		return new WebDriverWait(driver,Duration.ofSeconds(5)).until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
	        	return waitForElementVisibility(billStatus,5).getText().equals("refund");
	        }
		});
	}
	
	public String getRefundedAmount() {
		return driver.findElement(refundAmount).getText().substring(1);
	}
	
	public CashDrawerPage gotoCashDrawer() {
		getLogger().info("Navigating to Cash Drawer");
		driver.findElement(profileIcon).click();
		driver.findElement(cashDrawerBtn).click();
		
		return new CashDrawerPage(driver);
	}
	
	public DashboardPage gotoDashboard() {
		driver.findElement(profileIcon).click();
		driver.findElement(dashboardBtn).click();
		
		return new DashboardPage(driver);
	}
	
	public double getSubTotalAmount() {
		return Double.parseDouble(driver.findElement(subTotalAmount).getText().substring(1));
	}
	
	public double getTax() {
		return Double.parseDouble(driver.findElement(tax).getText().substring(1));
	}
	
	public double getServiceCharges() {
		return Double.parseDouble(driver.findElement(serviceCharge).getText().substring(1));
	}
	
	public double getTotalAmount() {
		return Double.parseDouble(driver.findElement(totalAmount).getText().substring(1));
	}
	
	public double getBalanceDue() {
		return Double.parseDouble(driver.findElement(balanceDue).getText().substring(1));
	}
	
	public String getRefundPrice() {
		return driver.findElement(refundPrice).getText().substring(1);
	}
	
	public String getItemRefundTotal() {
		return driver.findElement(refundAmountField).getText();
	}
	
	public void gotoRefundAmount() {
		getLogger().info("Navigating to Refund Amount");
		driver.findElement(refundAmountTab).click();
		
	}
	
	public void refundPartial() {
		getLogger().info("Editing Refund Amount");
		driver.findElement(refundAmountField).click();
		driver.findElement(backspaceBtn).click();
		driver.findElement(numpadDoneBtn).click();
		
	}
	
	public void tapPaymentNextBtn() {
		getLogger().info("Refunding Amount");
		driver.findElement(paymentNextBtn).click();
		
	}
	
	public String getTotalRefundAmount() {
		return driver.findElement(totalRefundAmount).getText().substring(1);	
	}
	
	public void uncheckAmount() {
		driver.findElement(refundAmountCheckbox).click();
	}
	
	public void uncheckTax() {
		driver.findElement(taxCheckbox).click();
	}
	
	public boolean isReasonDisplayed() {
		return driver.findElement(reasonNextBtn).isDisplayed();
	}
	
	public List<String> getItemsOnRefund(){
		List<WebElement> listItems=driver.findElements(itemName);
		List<String> itemNames=new ArrayList<String>();
		for(WebElement item:listItems) {
			itemNames.add(item.getText());
		}
		return itemNames;
	}
	
	public double getModifierPrice() {
		return Double.parseDouble(driver.findElement(modifierPrice).getText().substring(1));
	}
	
	public String getTableNumber() {
		return driver.findElement(tableNum).getText();
	}
	
	public void openFirstEntry() {
		waitForElementVisibility(order, 10).click();
	}
	
	public String getSource() {
		return driver.findElement(source).getText();
	}
	
	public void goToReadyOrders()
	{
		driver.findElement(readyBtn).click();
	}
	
	public String getTicketID() 
	{
	    String ticketId = driver.findElement(ticketNum).getText();
	    return ticketId;
	}
	public boolean isTicketDisplayed(String ticket) {
		getLogger().info("Checking for Ticket Status");
		return new WebDriverWait(driver,Duration.ofSeconds(5)).until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
	        	return waitForElementVisibility(ticketNum,10).getText().equals(ticket);
	        }
		});
	}
	
	public void openUnclosedTab() {
		getLogger().info("Clicking Unclosed Tab");
		driver.findElement(unclosed).click();
	}
	
	public String getRefundSalesTax() {
		getLogger().info("Getting Refund Sales Tax");
		return driver.findElement(By.id("rTax_Txt")).getText().replaceAll("[^0-9.]", "");
	}
}
