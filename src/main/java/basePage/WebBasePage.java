package basePage;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebBasePage {
	public WebDriver driver;
	public By title=By.id(":r3:");
	public By fixedPercentage=By.xpath("//label[normalize-space()='Fixed Percent']");
	public By percentageValue=By.id(":r7:");
	public By saveBtn=By.xpath("//span[normalize-space()='Save']");
	
	public WebBasePage(WebDriver driver) {
		this.driver=driver;
	}
	
	public WebElement waitForElementVisibility(By element,int time) {
		return new WebDriverWait(driver,Duration.ofSeconds(time)).until(ExpectedConditions.visibilityOfElementLocated(element));
	}
	
	public void addTitle(String name) {
		waitForElementVisibility(title,10).sendKeys(name);
	}
	
	public void selectFixedPercentage() {
		driver.findElement(fixedPercentage).click();
	}
	
	public void enterPercentAmount(String percent) {
		driver.findElement(percentageValue).sendKeys(percent);
	}
	
	public void save() {
		driver.findElement(saveBtn).click();
		try {
		Thread.sleep(10000);
		}
		catch(Exception e) {
			
		}
	}
}
