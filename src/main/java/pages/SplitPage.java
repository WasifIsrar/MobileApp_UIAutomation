package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import basePage.BasePage;
import io.appium.java_client.android.AndroidDriver;
import utils.LogUtils;

public class SplitPage extends BasePage{
	
	private By ticket=By.id("ClTicket");
	private By amount=By.id("TvAmount");
	private By splitItemBtn=By.id("btnSplitItem");
	private By item=By.id("Clitem");
	private By addTicketBtn=By.id("ClPlus");
	private By splitCount=By.id("edCount");
	private By tickets=By.id("RvItems");
	private By itemPrice=By.id("TvPrice");
	private By cancelPopup=By.id("llCancel");
	
	
	public SplitPage(AndroidDriver driver) {
		super(driver);
	}
	
	public boolean isScreenDisplayed() {
		getLogger().info("Waiting for Split Screen to display");
		return waitForElementPresent(splitTicketScreen,5).isDisplayed();
	}
	
	public void splitByGuests() {
		driver.findElement(splitTicketBtn).click();
		
	}
	
	public int getItemCount(String item) {
		return driver.findElements(By.xpath("//android.widget.TextView[@text='"+item+"']")).size();
	}
	
	public int getTicketCount() {
		getLogger().info("Getting Ticket Count");
		return driver.findElements(ticket).size();
	}
	
	public boolean verifyItemsSplit() {
		getLogger().info("Splitting Item and verifying");
		driver.findElement(item).click();
		driver.findElement(splitItemBtn).click();
		
		int count=Integer.parseInt(driver.findElement(splitCount).getText());
		driver.findElement(numpadDoneBtn).click();
		
		return driver.findElements(item).size()==count;
	}
	
	public boolean moveToTicket() {
		getLogger().info("Splitting Item by 2");
		driver.findElement(item).click();
		driver.findElement(splitItemBtn).click();
		
		driver.findElement(numpadDoneBtn).click();
		getLogger().info("Adding Ticket");
		driver.findElement(addTicketBtn).click();
		
		getLogger().info("Moving to Ticket");
		driver.findElement(item).click();
		WebElement secondTicket=driver.findElements(tickets).get(1);
		secondTicket.click();
		return secondTicket.findElements(item).size()>0;
		}
	
	public double getTotalCharges() {
		getLogger().info("Calculating commulative ammount");
		List<Double> amounts=new ArrayList<Double>();
		List<WebElement> amountList=driver.findElements(amount);
		for(WebElement amount:amountList) {
			amounts.add(Double.parseDouble(amount.getText().substring(1)));
		}
		double totalAmount=0;
		for(double amount:amounts) {
			totalAmount+=amount;
		}
        return totalAmount;
	}
	
	public void cancelSplit() {
		getLogger().info("Canceling Split");
		driver.findElement(cancelBtn).click();
		waitForElementPresent(cancelPopup,3);
		driver.findElements(cancelBtn).get(1).click();
		
	}
	
	public String getFirstItemPrice() {
		return driver.findElements(itemPrice).get(1).getText().substring(1);
	}
	
	public String getSecondItemPrice() {
		return driver.findElements(itemPrice).get(2).getText().substring(1);
	}
	
	public double getTotalTicketPrice() {
		return Double.parseDouble(driver.findElements(amount).get(0).getText().substring(1));
	}
	
	public List<Double> getTicketTotal() {
		List<Double> amounts=new ArrayList<Double>();
		List<WebElement> amountList=driver.findElements(amount);
		for(WebElement amount:amountList) {
			amounts.add(Double.parseDouble(amount.getText().substring(1)));
		}
		return amounts;
	}
}
