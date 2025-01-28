package pages;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import basePage.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import utils.LogUtils;

public class MposTablePage extends BasePage{

	private By myTables=By.id("tabText");
	private By occupiedTables=By.id("tvTableNo");
	private By layoutView=By.xpath("//android.widget.TextView[@text='Layout View']");
	private By tableNumber=By.id("tvtableNumber");
	private By floorSpinner=By.id("spinner");
	private By hamburgerMenu=By.id("ivDrawer");
	private By serverName=By.id("tvName");
	private By floorProgress=By.id("progress");
	private By guestPopup=By.id("includeGuestPopup");
	private By lockScreen = By.id("tvLock");
	private By progressIcon = By.id("progress");
	MposTablePage mPosTablePage;
	
	public MposTablePage(AndroidDriver driver) {
		super(driver);
	}
	
	public void continueHere() {
		waitForElementVisibility(continueBtn,10).click();
		
	}
	
	public void gotoMyTables() {
		waitForElementVisibility(myTables,5).click();
	}

	public List<String> getOccupiedTables() {
		List<WebElement> listTables=driver.findElements(occupiedTables);
		List<String> tableNames=new ArrayList<String>();
		for(WebElement table:listTables) {
			tableNames.add(table.getText());
		}
		if(listTables.size()>=7) {
			scrollToEnd("androidx.recyclerview.widget.RecyclerView");
			List<WebElement> listTables2=driver.findElements(occupiedTables);
			for(WebElement table:listTables2) {
				tableNames.add(table.getText());
			}
		}
		return tableNames.stream().distinct().collect(Collectors.toList());
	}
	
	public void gotoLayoutView() {
		driver.findElement(layoutView).click();
	}
	
	public WebElement findEmptyTable(List<String> occupiedTables) {
		getLogger().info("Finding Empty Table");
		WebElement botTable=driver.findElement(tableView);
		List<WebElement> tables=botTable.findElements(By.className("android.view.ViewGroup"));
		for(WebElement table:tables) {
			WebElement tableName=table.findElement(tableNumber);
			if(!occupiedTables.contains(tableName.getText())) {
				return tableName;
			}
		}
		return null;
	}
	
	public void openTable(WebElement table) {
		getLogger().info("Opening Table");
		table.click();
		
	}
	
	public MposOrderPage openTable(String table) {
		getLogger().info("Opening Table");
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+table+"']")).click();
		
		return new MposOrderPage(driver);
	}
	
	public MposOrderPage addGuestsInTable(String count) {
		getLogger().info("Adding Guests");
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+count+"']")).click();
		driver.findElement(doneBtn).click();
		
		return new MposOrderPage(driver);
	}
	
	private void scrollToEnd(String recyclerViewClassName) {
        // Construct the UiScrollable selector to scroll to end
        String uiScrollable = String.format(
            "new UiScrollable(new UiSelector().className(\"%s\")).scrollToEnd(10)", recyclerViewClassName
        );

        // Execute the UIAutomator command
        driver.findElement(AppiumBy.androidUIAutomator(uiScrollable));
	}
	
	public void openFloorSpinner() {
		driver.findElement(floorSpinner).click();
	}
	
	public void selectMposFloor() {
		driver.findElement(By.xpath("//android.widget.TextView[@text='mPos']")).click();
	}
	
	public void openHamburgerMenu() {
		driver.findElement(hamburgerMenu).click();
	}
	
	public String getServerName() {
		String fullName =  driver.findElement(serverName).getText();
		return fullName.split(" ")[0];
	}
	
	public void clickFloorProgress() {
		driver.findElement(floorProgress).click();
	}
	
	public boolean isGuestPopupDisplayed() {
		return driver.findElement(guestPopup).isDisplayed();
	}
	
	public void tapOnLockScreen() {
		driver.findElement(lockScreen).click();
	}
	
	public void clickOnProgressIcon() {
		driver.findElement(progressIcon).click();
	}

	public void openMyTable(String tableNumber) {
		WebElement tableToOpen = scrollTo(tableNumber, "androidx.recyclerview.widget.RecyclerView");
		tableToOpen.click();
	}



}
