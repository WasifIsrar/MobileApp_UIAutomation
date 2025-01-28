package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import basePage.WebBasePage;

public class DiscountPage extends WebBasePage{
	
	private By discountDropdown=By.cssSelector("div[role='combobox']");
	private By fixedOff=By.xpath("//li[normalize-space()='Fixed % off']");
	
	private By entireCheckBtn=By.xpath("//p[normalize-space()='Entire Check']");
	private By posName=By.id(":rb:");

	private By selectAllDays=By.xpath("//span[normalize-space()='Select All']");

	public DiscountPage(WebDriver driver) {
		super(driver);
	}
	
	public void selectDiscountDropdown() {
		driver.findElement(discountDropdown).click();
	}
	
	public void selectFixedPercentage() {
		driver.findElement(fixedOff).click();
	}
	
	public void checkEntire() {
		driver.findElement(entireCheckBtn).click();
	}
	
	public void enterPOSName(String discountName) {
		driver.findElement(posName).sendKeys(discountName);
	}
	
	public void selectDays() {
		driver.findElement(selectAllDays).click();
	}
}
