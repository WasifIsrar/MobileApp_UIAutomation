package pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import basePage.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import utils.LogUtils;

public class TablePage extends BasePage{

	private By doneBtn=By.id("btn_done");
	private By botFloor=By.xpath("//android.widget.TextView[@text='Bot']");
	private By settingsBtn=By.id("clSettings");
	private By confirmBtn=By.id("btnConfirm");
	private By floor=By.id("tvFloorItem");
	private By table=By.id("clMain");
	private By clockinBtn=By.id("tvClockIn");
	private By manager=By.xpath("//android.widget.TextView[@text='Manager']");
	private By name=By.id("tvName");
	private By loader=By.xpath("//android.widget.ImageView[contains(@resource-id,'loadingAnimationView')]");
	private By btnLock =By.id("btnLock");
	private By roleIcon=By.id("ivDummy");
	private By switchrolebtn=By.id("tvSwitch");
	private By serverItem= By.xpath("//android.widget.TextView[@text='Server']");
	private By clockInBtn = By.id("tvClockIn");
	private By dashboardBtn= By.id("tvDashBoard");
	private By tableNumber= By.id("tvtableNumber");

	
	public TablePage(AndroidDriver driver) {
		super(driver);
	}
	
	//Returns true if TableScreenDisplays
	public boolean isTableDisplayed(){
		getLogger().info("Waiting For Table Layout Screen");
		return waitForElementPresent(hamburgerMenu,10).isDisplayed();
	}
	
	public boolean isFloorPresent() {
		return driver.findElements(botFloor).size()==0;
	}
	
	public void verifyClockIn() {
		getLogger().info("Clocking In");
		driver.findElement(manager).click();
		driver.findElement(clockinBtn).click();
		getLogger().info("Waiting For Table Screen to display");
		isTableVisible();
	}
	
	public String findEmptyTable() {
		getLogger().info("Finding Empty Table");
		WebElement botTable=driver.findElement(tableView);
		List<WebElement> tables=botTable.findElements(By.className("android.view.ViewGroup"));
		for(WebElement table:tables) {
			List<WebElement> tableStatus=table.findElements(By.id("tableProgress"));
				if(tableStatus.size()==0) {
					getLogger().info("Empty Table Found");
					return table.findElement(By.id("tvtableNumber")).getText();
				}
		}
		return "Null";
	}
	
	public String getOccupiedTable() {
		WebElement botTable=driver.findElement(tables);
		List<WebElement> tables=botTable.findElements(By.className("android.view.ViewGroup"));
		for(WebElement table:tables) {
			List<WebElement> tableStatus=table.findElements(By.id("tableProgress"));
				if(tableStatus.size()>0) {
					return table.findElement(By.id("tvtableNumber")).getText();
				}
		}
		return "No Occupied Table";
	}
	
	public OrderPage addGuestsInTable(String count) {
		getLogger().info("Adding Guests");
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+count+"']")).click();
		driver.findElement(doneBtn).click();
		return new OrderPage(driver);
	}
	
	public void openTable(String tableNum) {
		getLogger().info("Opening table");
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+tableNum+"']")).click();
	}

//	public DashboardPage openDashboard() {
//		getLogger().info("Opening Dashboard");
//		driver.findElement(profileIcon).click();
//		driver.findElement(dashboardBtn).click();
//		LogUtils.captureLogcat(driver);
//		return new DashboardPage(driver);
//	}
	
	public CashDrawerPage gotoCashDrawer() {
		getLogger().info("Going to Cash Drawer");
		driver.findElement(profileIcon).click();
		driver.findElement(cashDrawerBtn).click();
		return new CashDrawerPage(driver);
	}
	public CashDrawerPage gotoCashLogs() {
		getLogger().info("Going to Cash Logs");
		driver.findElement(profileIcon).click();
		driver.findElement(cashLogs).click();
		return new CashDrawerPage(driver);
	}
	
	
	public SettingsPage gotoSettings() {
		getLogger().info("Opening Settings");
		driver.findElement(hamburgerMenu).click();
		driver.findElement(settingsBtn).click();
		return new SettingsPage(driver);
	}
	
	public void confirmTableMovement() {
		driver.findElement(confirmBtn).click();
	}
	
	public boolean verifyFloorAdded(String areaName) {
		List<WebElement> floors=driver.findElements(floor);
		boolean isFound=false;
		for(WebElement floor:floors) {
			if(floor.getText().equals(areaName)) {
				isFound=true;
				break;
			}
		}
		return isFound;
	}
	
	public void gotoFloor(String floorName,String tableNum) {
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+floorName+"']")).click();
		waitForElementVisibility(By.xpath("//android.widget.TextView[@text='"+tableNum+"']"),5);

	}
	
	public void gotoFloor(String floorName) {
		driver.findElement(By.xpath("//android.widget.TextView[@text='"+floorName+"']")).click();
	}
	
	public boolean verifyTablePresent(String tableName) {
		return driver.findElement(By.xpath("//android.widget.TextView[@text='"+tableName+"']")).isDisplayed();
	}
	
	public boolean isTableDeleted() {
		return driver.findElements(table).size()==0;
	}
	
	public List<String> getTableNumbers() {
		List<WebElement> listTableNumber=driver.findElements(tableNumber);
		List<String> tableNumbers=new ArrayList<String>();
		for(WebElement tableNumber:listTableNumber) {
			tableNumbers.add(tableNumber.getText());
		}
		return tableNumbers;
	}
	
	public boolean isAreaDeleted(String floorName) {
		return driver.findElements(By.xpath("//android.widget.TextView[@text='"+floorName+"']")).size()==0;
	}
	
	public String getServerInitials(String tableNum) {
		return driver.findElement(By.xpath("//android.widget.TextView[@text='"+tableNum+"']/following-sibling::android.widget.TextView[contains"
				+ "(@resource-id,'tvServerName')]")).getText();
	}
	
	public String getServerName() {
		return driver.findElement(name).getText();
	}
	
	public void waitForLoader() {
		waitForElementPresent(loader,5);
	}
	public void clickHamburger() {
		driver.findElement(hamburgerMenu).click();	
	}	
	public void checkContinueHere() {
		List<WebElement> listContinueBtn=driver.findElements(continueBtn);
		if(listContinueBtn.size()>0) {
			listContinueBtn.get(0).click();
		}
	}
	public PinPage lockScreen() {
		driver.findElement(btnLock).click();	
		return new PinPage(driver);
	}
	public void clickRoleIcon() {
		driver.findElement(roleIcon).click();	
	}
	public void clickSwitchRoleBtn() {
		driver.findElement(switchrolebtn).click();	
	}
	public void clickServer() {
		driver.findElement(serverItem).click();	
	}
	public void clickClockInBtn() {
		driver.findElement(clockInBtn).click();	
	}
	public DashboardPage clickDashboardBtn() {
		driver.findElement(dashboardBtn).click();
		return new DashboardPage(driver);
	}
	
}
