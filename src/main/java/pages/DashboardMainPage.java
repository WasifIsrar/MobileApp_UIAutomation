package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import basePage.WebBasePage;

public class DashboardMainPage extends WebBasePage{

	private By navBtn=By.xpath("//div[contains(@class,'mui-kjafn5')]/child::button[1]");
	private By menuItems=By.xpath("//ul/child::*");
	private By menuOptions=By.xpath("//div[contains(@class,'mui-1e8s1u9-MuiList-root')]/child::*");
	private By createDiscountBtn=By.xpath("//span[normalize-space()='Create Discount']");
	private By createServiceChargesBtn=By.xpath("//span[normalize-space()='Create Service Charge']");
	

	public DashboardMainPage(WebDriver driver) {
		super(driver);
	}

	public void clickNavBtn() {
		waitForElementVisibility(navBtn,10).click();
	}
	
	public void selectPayments() {
		driver.findElements(menuItems).get(2).click();
	}
	
	public void selectDiscounts() {
		driver.findElements(menuOptions).get(1).click();
	}
	
	public void selectServiceCharges() {
		driver.findElements(menuOptions).get(2).click();
	}
	
	public DiscountPage createDiscount() {
		waitForElementVisibility(createDiscountBtn,10).click();
		return new DiscountPage(driver);
	}
	
	public ServiceChargesPage createServiceCharges() {
		driver.findElement(createServiceChargesBtn).click();
		return new ServiceChargesPage(driver);
	}
}
