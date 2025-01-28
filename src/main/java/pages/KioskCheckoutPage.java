package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;
import io.appium.java_client.android.AndroidDriver;

public class KioskCheckoutPage extends BasePage{

	private By payCashBtn=By.id("tvByCash");
	private By quantity=By.id("tvQuantity");
	private By price=By.id("tvPrice");
	private By addQuantity=By.id("ivQuantityAdd");
	private By subtotal=By.id("tvAmount");
	private By subtotalAmountPayment=By.id("tvSubtotalAmount");
	private By totalDue=By.id("tvTotalDueAmount");
	private By item=By.id("tvItemName");
	private By modifiers=By.id("tvModifiers");
	private By addMoreBtn=By.id("rvAddMore");
	private By taxAmount=By.id("tvTaxAmount");
	private By serviceCharge=By.id("tvConvinenceFeeAmount");
	private By proceedBtn=By.id("tvProceed");
	private By totalPaymentCash=By.id("tvTotalPaymentCash");
	private By goBackBtn=By.id("tvGoBack");
	private By cancelBtn = By.id("btnCancel");
	private By cancelOrderBtn = By.id("tvEmptyCart");
	private By totalItem = By.xpath("//androidx.recyclerview.widget.RecyclerView[contains(@resource-id, 'rvCartItems')]//android.view.ViewGroup[contains(@resource-id, 'itemView')]");
	private By payByCard = By.id("tvByCard");
	private By tip = By.id("tvTipAmount");
	private By selectTip = By.xpath("//android.widget.TextView[contains(@resource-id, 'tvTip') and contains(@text, 'No Tip')]");
	private By proceedToPayment = By.id("tvProceed");
	private By skip = By.id("tvSkip");
	private By next = By.id("tvNext");
	private By getReceiptText = By.id("figmaTextView3");
	private By getOrderNumber = By.id("figmaTextView2");

			
	
	public KioskCheckoutPage(AndroidDriver driver) {
		super(driver);
	}
	
	public void checkout() {
		driver.findElement(checkoutBtn).click();
	}
	
	public void payByCash() {
		driver.findElement(payCashBtn).click();	
	}
	
	public int getQuantity() {
		return Integer.parseInt(driver.findElement(quantity).getText());
	}
	
	public double getPrice() {
		return Double.parseDouble(driver.findElement(price).getText().substring(1));
	}
	
	public void addQuantity() {
		driver.findElement(addQuantity).click();
	}
	
	public double getSubtotal() {
		return Double.parseDouble(driver.findElement(subtotal).getText().substring(1));
	}
	
	public double getSubtotalPaymentAmount() {
		return Double.parseDouble(driver.findElement(subtotalAmountPayment).getText().substring(1));
	}
	
	public void waitForBalanceDue() {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(5));
		wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
				return Double.parseDouble(driver.findElement(totalDue).getText().substring(1))>0;
	        }
		});
	}
	
	public void clickItem() {
		waitForElementVisibility(item, 5).click();
	}
	
	public void clickItemWithWait() {
		waitForElementVisibility(item, 5);
	}
	
	public String getModifiers() {
		return driver.findElement(modifiers).getText();
	}
	
	public void addMore() {
		driver.findElement(addMoreBtn).click();
	}
	
	public int getItemCount() {
		return driver.findElements(item).size();
	}
	
	public double getTaxAmount() {
		return Double.parseDouble(driver.findElement(taxAmount).getText().substring(1));
	}
	
	public double getServiceCharge() {
		return Double.parseDouble(driver.findElement(serviceCharge).getText().substring(1));
	}
	
	public double getTip() {
		return Double.parseDouble(driver.findElement(tip).getText().substring(1));
	}
	
	public double getTotalDue() {
		return Double.parseDouble(driver.findElement(totalDue).getText().substring(1));
	}
	
	public void tapProceed() {
		driver.findElement(proceedBtn).click();
	}
	
	public void waitForPaymentCash() {
		waitForElementVisibility(totalPaymentCash,5);
	}
	
	public void skip() {
		driver.findElement(goBackBtn).click();
	}
	
	public void tapCancelBtn() {
		driver.findElement(cancelBtn).click();
	}
	
	public void tapCancelOrderBtn() {
		waitForElementVisibility(cancelOrderBtn, 5).click();
	}
	
	public int itemCount() {
		return driver.findElements(totalItem).size();
	}
	
	public void tapPayByCard() {
		driver.findElement(payByCard).click();
	}
	
	public void waitForGoback() {
		waitForElementVisibility(goBackBtn, 5);
	}
	
	public void tapNoTip() {
		driver.findElement(selectTip).click();
	}
	
	public void tapProceedToPayment() {
		driver.findElement(proceedToPayment).click();
	}
	
	public void tapSkip() {
		driver.findElement(skip).click();
	}
	
	public void tapNext() {
		waitForElementToBeClickable(next, 5).click();
	}
	
	public void getReceiptText() {
		waitForElementVisibility(getReceiptText, 20);
	}

	public String getOrderNumber() {
		String getText = driver.findElement(getOrderNumber).getText();
		String[] getOrder = getText.split(" ");
		return getOrder[getOrder.length - 1];
	}
}
