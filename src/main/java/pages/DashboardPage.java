package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;

import basePage.BasePage;
import io.appium.java_client.android.AndroidDriver;
import utils.LogUtils;

public class DashboardPage extends BasePage{
	
	private By salesAmount=By.id("tvSales");
	private By actualSalesAmount=By.xpath("(//android.widget.TextView[contains(@resource-id,'tvSales')])[2]");

	private By declareCashBtn=By.id("btnDeclareCash");
	private By declareTipsBtn=By.id("btnDeclareTips");
	private By clockInTime=By.id("tvClockInTime");
	private By zReportTab=By.id("tvZReport");
	private By netSales=By.xpath("//android.widget.TextView[@text='Total Net Sales']/following-sibling::android.widget.TextView[contains(@resource-id,'tvValue')]");
	private By taxes=By.xpath("//android.widget.TextView[@text='Tax']/following-sibling::android.widget.TextView[contains(@resource-id,'tvValue')]");
	private By surcharges=By.xpath("//android.widget.TextView[@text='Surcharges']/following-sibling::android.widget.TextView[contains(@resource-id,'tvValue')]");
	private By gratuity=By.xpath("//android.widget.TextView[@text='Gratuity']/following-sibling::android.widget.TextView[contains(@resource-id,'tvValue')]");
	private By voidAmount=By.xpath("//android.widget.TextView[@text='Void Amount']/following-sibling::android.widget.TextView[contains(@resource-id,'tvValue')]");
	private By voidOrderCount=By.xpath("//android.widget.TextView[@text='Void Order Count']/following-sibling::android.widget.TextView[contains(@resource-id,'tvValue')]");
	private By voidItemCount=By.xpath("//android.widget.TextView[@text='Void Item Count']/following-sibling::android.widget.TextView[contains(@resource-id,'tvValue')]");
	private By totalPayments=By.xpath("//android.widget.TextView[@text='Total Payments']/following-sibling::android.widget.TextView[contains(@resource-id,'tvValue')]");
	private By voidPercent=By.xpath("//android.widget.TextView[@text='Void Percent']/following-sibling::android.widget.TextView[contains(@resource-id,'tvValue')]");
	private By overviewTab=By.id("ivOverView");
	private By refunds=By.id("tvRefunds");
	private By unclosedCheckCount=By.id("tvFirst");
	private By day=By.id("tvDay");
	private By totalSales=By.id("tvTotalSales");
	private By totalChecks=By.id("tvTotalChecks");
	private By totalTips=By.id("tvTotalTips");
	private By guestsServed=By.id("tvTotalGuestsServed");
	private By cashAmount=By.xpath("//android.widget.TextView[@text='Cash']/following-sibling::android.widget.TextView[contains(@resource-id,'tvValue')]");
	private By voided=By.id("tvVoided");
	private By totalSalesTax=By.id("tvTotalSalesTax");
	private By unpaidChecksBtn=By.id("ivFirst");
	private By clockInList=By.id("rvClockIn");
	private By totalComps=By.id("tvTotalComps");
	private By tender = By.xpath("//android.widget.TextView[@text='Tender']/following-sibling::android.widget.TextView[contains(@resource-id,'tvValue')]");
	private By getList =By.id("ivClockIn");
	private By overAllTipPercent = By.id("tvTotalOverallTip");

	public DashboardPage(AndroidDriver driver) {
		super(driver);
	}
	
	public void isPageDisplayed() {
		getLogger().info("Waiting For dashboard screen to display");
		
		new WebDriverWait(driver,Duration.ofSeconds(5)).until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
				WebElement sales=driver.findElement(salesAmount);
				String amount=sales.getText();
				return !amount.isEmpty() ;
				}
		});
	}
	
	/*
	 * Returns Sales Amount from dashboard
	 */
	public String getSales() {
		new WebDriverWait(driver,Duration.ofSeconds(5)).until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
				WebElement sales=driver.findElement(salesAmount);
				String amount=sales.getText();
				return !amount.isEmpty() ;
				}
	        	//return removeComma(!driver.findElements(salesAmount).get(1).getText().isEmpty();
		});
		return removeComma(driver.findElement(salesAmount).getText().substring(1));
	}
	
	public void goBack() {
		getLogger().info("Going back from Dashboard");
		driver.findElement(backBtn).click();
		
	}
	
	public void declareCash() {
		getLogger().info("Declaring Cash");
		driver.findElement(declareCashBtn).click();
		
		driver.findElement(btn1).click();
		WebElement zeroBtn=driver.findElement(btn0);
		for(int i=0;i<3;i++) {
			zeroBtn.click();
		}
		driver.findElement(numpadDoneBtn).click();
		
	}
	
	public void declareTips() {
		driver.findElement(declareTipsBtn).click();
		
		driver.findElement(btn1).click();
		WebElement zeroBtn=driver.findElement(btn0);
		for(int i=0;i<3;i++) {
			zeroBtn.click();
		}
		driver.findElement(numpadDoneBtn).click();
		
		waitForElementVisibility(backBtn,3);
	}
	
	public boolean getClockInTime(String deviceTime) {
		WebElement listClockIns=driver.findElement(clockInList);
		if(listClockIns.findElements(By.className("android.view.ViewGroup")).size()>2) {
			((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", ImmutableMap.of(
					"elementId",((RemoteWebElement)listClockIns).getId(),
					"direction", "down",
					"percent", 10.0
			));
		}
		List<WebElement> times=driver.findElements(clockInTime);
		boolean isTime=false;
		for(WebElement time:times) {
			if(time.getText().equalsIgnoreCase(deviceTime)) {
				isTime=true;
				break;
			}
		}
		return isTime;
	}
	
	public void openZReport() {
		getLogger().info("Opening Z Report");
		waitForElementVisibility(zReportTab,5).click();
		new WebDriverWait(driver,Duration.ofSeconds(5)).until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
				WebElement sales=driver.findElement(netSales);
				String amount=sales.getText();
				return !amount.isEmpty() ;
				}
		});
	}
	
	public OrdershubPage openUnpaidChecks() {
		driver.findElement(unpaidChecksBtn).click();
		return new OrdershubPage(driver);
	}
	
	public double getTotalTips() {
		return Double.parseDouble(driver.findElement(totalTips).getText());
	}
	public String getNetSales() {
		return removeComma(waitForElementVisibility(netSales,5).getText().substring(1));
	}
	
	public String getTaxes() {
		return removeComma(driver.findElement(taxes).getText().substring(1));
	}
	
	public String getVoidAmount() {
		return removeComma(driver.findElement(voidAmount).getText().substring(1));
	}
	
	public int getVoidOrderCount() {
		return Integer.parseInt(driver.findElement(voidOrderCount).getText());
	}
	
	public int getVoidItemCount() {
		return Integer.parseInt(driver.findElement(voidItemCount).getText());
	}
	
	public String getTotalPayments() {
		return removeComma(driver.findElement(totalPayments).getText().substring(1));
	}
	
	public String getVoidPercent() {
		return driver.findElement(voidPercent).getText();
	}
	
	public void openOverview() {
		getLogger().info("Navigating to Overview Tab");
		waitForElementVisibility(overviewTab, 10).click();
		new WebDriverWait(driver,Duration.ofSeconds(5)).until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
				WebElement sales=driver.findElement(actualSalesAmount);
				String amount=sales.getText();
				return !amount.isEmpty() ;
				}
		});
	}
	
	public String getRefunds() {
		return removeComma(driver.findElement(refunds).getText());
	}
	
	public int getUnclosedChecks() {
		return Integer.parseInt(driver.findElement(unclosedCheckCount).getText().replaceAll("[^0-9]",""));
	}
	
	public String getActualSales() {
		new WebDriverWait(driver,Duration.ofSeconds(5)).until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
				WebElement sales=driver.findElements(salesAmount).get(1);
				String amount=sales.getText();
				return !amount.isEmpty() ;
				}
	        	//return removeComma(!driver.findElements(salesAmount).get(1).getText().isEmpty();
		});
		return removeComma(driver.findElements(salesAmount).get(1).getText());
	 }
	
	public String getTotalSales() {
		return removeComma(driver.findElement(totalSales).getText());
	}
	public double getTotalSaleAmount() {
		return Double.parseDouble(removeComma(driver.findElement(totalSales).getText()));
	}
	
	public double getOverAllTipPercent() {
		return Double.parseDouble(removeComma(driver.findElement(overAllTipPercent).getText()));
	}
	public int getTotalChecks() {
		return Integer.parseInt(driver.findElement(totalChecks).getText());
	}
	
	public int getGuestsServed() {
		return Integer.parseInt(driver.findElement(guestsServed).getText());
	}
	
	public String getCashAmount() {
		return removeComma(driver.findElement(cashAmount).getText().substring(1)); 
	}
	
	public String getVoided() {
		return removeComma(driver.findElement(voided).getText());
	}
	
	public String getTotalSalesOnZReport() {
		return removeComma(waitForElementVisibility(totalSalesTax,5).getText()); 
	}
	
	private String removeComma(String amount) {
		if(amount.contains(",")){
			amount=amount.replace(",","");
		}
		return amount;	
	}
	
	public String getSurcharges() {
		return removeComma(driver.findElement(surcharges).getText().substring(1));
	}
	
	public String getGratuity() {
		return removeComma(driver.findElement(gratuity).getText().substring(1));
	}
	
	public double getTotalComps() {
		return Double.parseDouble(driver.findElement(totalComps).getText());
	}
	
	public double getTenderAmount() {
		return Double.parseDouble(removeComma(driver.findElement(tender).getText().substring(1)));
	}
	public boolean isMyDayDisplayed() {
		 List<WebElement> elements = driver.findElements(day);
		 return elements.size() == 0;
	}
}
