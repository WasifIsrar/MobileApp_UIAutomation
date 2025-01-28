package baseTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import pages.KDS;
import pages.OrderPage;
import pages.PinPage;
import pages.SplitPage;
import pages.TablePage;
import utils.FileUtility;

public class POSKDSBaseTest extends BaseTest{
	
	public AndroidDriver driver1;
	public AndroidDriver driver2;
	public TablePage tablePage;
	public OrderPage orderPage;
	public SplitPage splitPage;
	public KDS kds;
	public String tableNum; 
	public String ticketId;
	String pin;
	
	@BeforeMethod(alwaysRun=true)
	@Parameters({"udid","udid2","pin"})
	public void startApps(String udid,String udid2,Method method,String pin) throws MalformedURLException {
		String env=FileUtility.readEnvironmentFromFile();
		this.pin=pin;
		UiAutomator2Options options1=new UiAutomator2Options();
		options1.setCapability("udid",udid);
		options1.setApp(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"resources"+File.separator+"app-"+env+"-release.apk");
		options1.setCapability("autoGrantPermissions",true);
		options1.setSystemPort(8300);
		options1.setNewCommandTimeout(Duration.ofSeconds(600));
		try {
		driver1=new AndroidDriver(new URL("http://localhost:4723"),options1);
		}
		catch(Exception e) {
			getLogger().error("Driver1 Not Started "+e);
		}
		driver1.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		UiAutomator2Options options2=new UiAutomator2Options();
		options2.setCapability("udid",udid2);
		options2.setApp(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"resources"+File.separator+"app-"+env+"-release.apk");
		options2.setCapability("autoGrantPermissions",true);
		options2.setSystemPort(8400);
		options2.setNewCommandTimeout(Duration.ofSeconds(600));
		try {
		driver2=new AndroidDriver(new URL("http://localhost:4725"),options2);
		}
		catch(Exception e) {
			getLogger().error("Driver2 Not Started "+e);
		}
		driver2.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		kds=new KDS(driver2);	
		getLogger().info("Starting test: " + method.getName());
	}
	
	/*
	 * Voids Ticket
	 * If or not exception occurs in voidTicket
	 * quit drivers
	 */	
	@AfterMethod(alwaysRun=true)
	public void voidAndQuit(ITestResult result) {
		if(driver1!=null) {
		try {
			orderPage.openKebabMenu();
			orderPage.voidTicket();
			orderPage.waitForTables();
			}
		catch(Exception e) {
			getLogger().info("Not on Order Screen");
			}
		finally {
			driver1.quit();
			}
		}
		if(driver2!=null) {
			driver2.quit();
		}
		getLogger().info("Finished Test: " + result.getMethod().getMethodName());
	}
	
	public void enterPin() {
		PinPage pinPage=new PinPage(driver1);
		tablePage=pinPage.enterPin(pin.toCharArray());
	}
	
	public void addGuestsAndGetTicketId() {
		String table=tablePage.findEmptyTable();
		tablePage.openTable(table);
		orderPage=tablePage.addGuestsInTable("5");
		orderPage.isPageDisplayed();
		tableNum=orderPage.getTableNumber();
		ticketId=orderPage.getTicketId();
		kds.setTicketId(ticketId);
	}
}
