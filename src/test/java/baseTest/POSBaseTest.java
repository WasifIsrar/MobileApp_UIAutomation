package baseTest;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import pages.CashDrawerPage;
import pages.DashboardPage;
import pages.OrderPage;
import pages.OrdershubPage;
import pages.PaymentPage;
import pages.PinPage;
import pages.SettingsPage;
import pages.SplitPage;
import pages.TablePage;
import utils.FileUtility;

public class POSBaseTest extends BaseTest{
	private static ThreadLocal<AndroidDriver> driverThreadLocal = new ThreadLocal<>();
	public AndroidDriver driver1;
	public TablePage tablePage;
	public CashDrawerPage cashDrawerPage;
	public OrderPage orderPage;
	public SplitPage splitPage;
	public PaymentPage paymentPage;
	public PinPage pinPage;
	public DashboardPage dashboardPage;
	public OrdershubPage ordershubPage;
	public SettingsPage settingsPage;
	public String deviceTime;
	public int ticketCount;
	public String tableNum;
	public String ticketId;
	protected String serverPin;
	protected String managerPin;
	String floor;
	String table;
	
	protected AndroidDriver getDriver() {
	    return driverThreadLocal.get();
	}

	protected void setDriver(AndroidDriver driver) {
	    driverThreadLocal.set(driver);
	}
	
	@BeforeMethod(alwaysRun=true)
	@Parameters({"udid","serverPin","systemPort","managerPin","appiumPort"})
	public void startApp(String udid,Method method,String serverPin,int systemPort,String managerPin,int appiumPort) {
		this.serverPin=serverPin;
		this.managerPin=managerPin;
		String env=FileUtility.readEnvironmentFromFile();
		UiAutomator2Options options1=new UiAutomator2Options();
		options1.setCapability("udid",udid);
		options1.setApp(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"resources"+File.separator+"app-"+env+"-release.apk");
		options1.setCapability("autoGrantPermissions",true);
		options1.setSystemPort(systemPort);
		try {
			driver1=new AndroidDriver(new URL("http://localhost:"+appiumPort),options1);
			setDriver(driver1);
		}
		catch(Exception e) {
			getLogger().error("Driver1 Not Started "+e);	
		}
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		getLogger().info("Starting test: " + method.getName());
	}
	
	@AfterMethod(alwaysRun=true)
	public void tearDown(ITestResult result) {
		AndroidDriver driver1=getDriver();
		if(driver1!=null) {
			try {
				orderPage.openKebabMenu();
				orderPage.voidTicket();
				orderPage.waitForTables();
				}
			catch(Exception e) {
				getLogger().info("Not on Orders Screen "+e);
				}
			finally {
				driver1.quit();
				}
			}
		getLogger().info("Finished Test: " + result.getMethod().getMethodName());

	}
	
	public void enterServerPin() {
		pinPage=new PinPage(getDriver());
		tablePage=pinPage.enterPin(serverPin.toCharArray());
		deviceTime=tablePage.getDeviceTime();
	}
	public void enterPin() {
		pinPage=new PinPage(getDriver());
		tablePage=pinPage.enterPin(managerPin.toCharArray());
		deviceTime=tablePage.getDeviceTime();
	}
	
	public void isTableScreenDisplayed() {
		tablePage.isTableVisible();
	}
	

	/*
	 * Goes to Bot Floor, Adds Guests, Adds Item
	 */
	public void addGuestsAndItem(String guests) {
		String table=tablePage.findEmptyTable();
		tablePage.openTable(table);
		orderPage=tablePage.addGuestsInTable(guests);
		orderPage.isPageDisplayed();
		tableNum=orderPage.getTableNumber();
		ticketId=orderPage.getTicketId();
		orderPage.addItem();
	}
	
	public List<Double> splitItemPrice(double amount,int parts){
		double partPrice=(amount/parts);
		List<Double> result=new ArrayList<Double>();
		String formatPartPrice=String.format("%.2f", partPrice);
		for(int i=0;i<parts-1;i++) {
			result.add(Double.parseDouble(formatPartPrice));
		}
		double total=Double.parseDouble(formatPartPrice)*(parts-1);
		String formattedTotal=String.format("%.2f", total);
		double lastGuestPrice=amount-Double.parseDouble(formattedTotal);
		result.add(lastGuestPrice);
		return result;
	}
		
}
