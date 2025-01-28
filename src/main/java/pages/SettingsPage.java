package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.google.common.collect.ImmutableMap;

import basePage.BasePage;
import io.appium.java_client.android.AndroidDriver;

public class SettingsPage extends BasePage{
	
	private By devicesTab=By.id("tvDevices");
	private By deviceSettings=By.xpath("(//android.widget.ImageView[contains(@resource-id,'ivSettings')])[2]");
	private By deviceName=By.id("etDeviceName");
	private By saveChangesBtn=By.id("btnSaveChanges");
	private By tableLayoutBtn=By.id("tvTableLayout");
	private By addNewAreaBtn=By.id("btnAddNewArea");
	private By areaField=By.id("edAreaName");
	private By saveBtn=By.id("btnSave");
	private By square=By.id("ivSquare");
	private By tableNumberField=By.id("edTableNumber");
	private By kdsTab=By.id("tvKds");
	private By printerHeading = By.id("tvPrinterHeading");
	private By kdsHeading=By.id("tvkdsHeading");
	private By areaPreview=By.id("tvPreview");
	private By dropdown=By.id("spinner");
	private By editLayoutBtn=By.id("btnEditLayout");
	private By removeTableBtn=By.id("btnRemove");
	private By deleteAreaBtn=By.id("btnDeleteArea");
	private By deletePopupBtn=By.id("btnDelete");
	private By proceedBtn=By.id("btnProceed");
	
	
	public SettingsPage(AndroidDriver driver) {
		super(driver);
	}
	
	private void clickTab(By tabLocator) {
	    driver.findElement(tabLocator).click();
	}

	private void renameDevice(String newName) {
	    driver.findElement(deviceSettings).click();
	    driver.findElement(deviceName).clear();
	    driver.findElement(deviceName).sendKeys(newName);
	    driver.findElement(saveChangesBtn).click();
	}

	private boolean isConfirmationMessageDisplayed() {
	    return driver.findElement(confirmationMessage).isDisplayed();
	}

	public boolean verifyDeviceRenamed(String deviceType, String newDeviceName) {
	    getLogger().info("Renaming " + deviceType);
	    clickTab(devicesTab);  
	    waitForElementVisibility(printerHeading, 3);
	    
	    if (deviceType == "KDS") {
	        clickTab(kdsTab); 
	        waitForElementVisibility(kdsHeading, 3); 
	    }
	    
	    renameDevice(newDeviceName);  
	    return isConfirmationMessageDisplayed(); 
	}


	
	public void createFloor() {
		getLogger().info("Adding Area");
		driver.findElement(addNewAreaBtn).click();
		driver.findElement(areaField).sendKeys("Bot");
		driver.findElement(saveBtn).click();
		getLogger().info("Adding 10 Tables");
		WebElement squareIcon=waitForElementPresent(square,5);
		int x=500;
		int y=400;
		int tableNum=1;
		for(int i=0;i<10;i++) {
			((JavascriptExecutor) driver).executeScript("mobile: dragGesture", ImmutableMap.of(
					"elementId", ((RemoteWebElement) squareIcon).getId(),
					"endX", x,
					"endY", y
					));
			driver.findElement(tableNumberField).sendKeys("B"+tableNum);
			driver.findElement(saveBtn).click();
			x=x+150;
			tableNum=tableNum+1;
		}
		driver.findElement(saveChangesBtn).click();
		waitForElementVisibility(areaPreview,5);
	}
	
	public void gotoTableLayout() {
		getLogger().info("Opening Table Layout");
		driver.findElement(tableLayoutBtn).click();
	}
	
	public void verifyAreaAdded(String areaName) {
		getLogger().info("Adding New Area");
		driver.findElement(addNewAreaBtn).click();
		driver.findElement(areaField).sendKeys(areaName);
		driver.findElement(saveBtn).click();
		driver.findElement(saveChangesBtn).click();
		waitForElementVisibility(areaPreview,5);
	}
	
	public void editLayout() {
		getLogger().info("Editing Layout");
		driver.findElement(editLayoutBtn).click();
		getLogger().info("Waiting for Shapes Screen");
		waitForElementPresent(square,3);
	}
	
	public void addTableToArea(String tableNum) {
		getLogger().info("Adding Table to Area");
		WebElement squareIcon=driver.findElement(square);
		((JavascriptExecutor) driver).executeScript("mobile: dragGesture", ImmutableMap.of(
				"elementId", ((RemoteWebElement) squareIcon).getId(),
				"endX", 550,
				"endY", 415
				));
		driver.findElement(tableNumberField).sendKeys(tableNum);
		driver.findElement(saveBtn).click();
	}
	
	public void saveChanges() {
		getLogger().info("Saving Changes");
		driver.findElement(saveChangesBtn).click();
		waitForElementVisibility(areaPreview,5);
	}
	
	public void selectArea(String areaName) {
		getLogger().info("Selecting Area");
		driver.findElement(dropdown).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+areaName+"']")).click();
	}
	
	public String verifyAreaDeleted() {
		driver.findElement(deleteAreaBtn).click();
		driver.findElement(deletePopupBtn).click();
		return driver.findElement(confirmationMessage).getText();
	}
	
	public void deleteTable(String tableNum) {
		getLogger().info("Deleting Table");
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+tableNum+"']")).click();
		driver.findElement(removeTableBtn).click();
	}
	
	public boolean verifyOccupiedStatus() {
		return driver.findElement(confirmationMessage).getText().equals("Table is occupied");
	}
	
	public void cancelChanges() {
		getLogger().info("Canceling Changes");
		driver.findElement(cancelBtn).click();
		driver.findElement(proceedBtn).click();
	}
	
	public boolean isTableDisplayed(String tableNum) {
		return driver.findElement(By.xpath("//android.widget.TextView[@text='"+tableNum+"']")).isDisplayed();
	}
}
