package tests;

import java.io.File;
import java.time.Duration;
import java.net.URL;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import pages.KDS;
import pages.OrderPage;
import pages.PaymentPage;
import pages.PinPage;
import pages.TablePage;

public class TwoKDSTest {
	public AndroidDriver driverPos;
	public AndroidDriver driver;
	public AndroidDriver driverKDS1;
	public AndroidDriver driverKDS2;
	public PinPage pinPage;
	public TablePage tablePage;
	public OrderPage orderPage;
	public PaymentPage paymentPage;
	public KDS kds1;
	public KDS kds2;
	public int ticketCount;
	public String tableNum;
	public String ticketId;
	
	@BeforeClass
	public void beforeClass() {
		UiAutomator2Options options1=new UiAutomator2Options();
		options1.setCapability("udid","emulator-5554");
		options1.setApp(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"resources"+File.separator+"app-internal-release.apk");
		options1.setCapability("autoGrantPermissions",true);
		options1.setSystemPort(8300);
		options1.setNewCommandTimeout(Duration.ofSeconds(600));
		try {
		driverPos=new AndroidDriver(new URL("http://localhost:4723"),options1);
		}
		catch(Exception e) {
			
		}
		driverPos.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		UiAutomator2Options options2=new UiAutomator2Options();
		options2.setCapability("udid","emulator-5556");
		options2.setApp(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"resources"+File.separator+"app-internal-release.apk");
		options2.setCapability("autoGrantPermissions",true);
		options2.setSystemPort(8400);
		options2.setNewCommandTimeout(Duration.ofSeconds(600));
		try {
		driverKDS1=new AndroidDriver(new URL("http://localhost:4725"),options2);
		}
		catch(Exception e) {
			
		}
		driverKDS1.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		UiAutomator2Options options3=new UiAutomator2Options();
		options3.setCapability("udid","emulator-5558");
		options3.setApp(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"resources"+File.separator+"app-internal-release.apk");
		options3.setCapability("autoGrantPermissions",true);
		options3.setSystemPort(8500);
		options3.setNewCommandTimeout(Duration.ofSeconds(600));
		try {
		driverKDS2=new AndroidDriver(new URL("http://localhost:4727"),options3);
		}
		catch(Exception e) {
			
		}
		driverKDS2.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver=driverPos;
	}
		@Test
		public void twoKDS() {
			PinPage pinPage=new PinPage(driverPos);
			tablePage=pinPage.enterPin("962838".toCharArray());
			driver=driverKDS1;
			kds1=new KDS(driverKDS1);
			kds1.confirmStation();
			driver=driverKDS2;
			kds2=new KDS(driverKDS2);
			kds2.selectAllItemsStation();
			kds2.confirmStation();
			driver=driverPos;
			String tableNum=tablePage.findEmptyTable();
			tablePage.openTable(tableNum);
			orderPage=tablePage.addGuestsInTable("5");
			orderPage.isPageDisplayed();
			String ticketId=orderPage.getTicketId();
			orderPage.addFirstItem();
			orderPage.addItem();
			orderPage.stay();
			String fulfilledItem=orderPage.getItemNames().get(0);
			driver=driverKDS1;
			kds1.setTicketId(ticketId);
			kds1.getTicketNumber();
			driver=driverKDS2;
			kds2.setTicketId(ticketId);
			kds2.getTicketNumber();
			driver=driverKDS1;
			kds1.openTicket();
			kds1.selectAll();
			kds1.fulfill();
			kds1.isDisappear();
			driver=driverKDS2;
			kds2.openTicket();
			Assert.assertTrue(kds2.verifyFulfilled(fulfilledItem),"Item not fulfilled on All Items Station: ");
			
		}
		@Test
		public void checkCustomItemsOfTwoKDS() {
			pinPage=new PinPage(driverPos);
			tablePage=pinPage.enterPin("333471".toCharArray());
			driver=driverKDS1;
			kds1=new KDS(driverKDS1);
//			kds1.selectFirstStation();
			kds1.confirmStation();
			driver=driverKDS2;
			kds2=new KDS(driverKDS2);
//			kds2.selectFirstStation();
			kds2.confirmStation();
			driver=driverPos;
			String tableNum=tablePage.findEmptyTable();
			tablePage.openTable(tableNum);
			orderPage=tablePage.addGuestsInTable("5");
			orderPage.isPageDisplayed();
			String ticketId=orderPage.getTicketId();
			System.out.println(ticketId);
			orderPage.openKebabMenu();
			orderPage.addCustomItem("Biryani");
			orderPage.stay();
			kds1.getItemsCount();
			Assert.assertTrue(kds1.isTicketIdMatchingInKDS(ticketId), "Ticket ID from orderPage does not match any in KDS.");
			Assert.assertTrue(kds2.isTicketIdMatchingInKDS(ticketId), "Ticket ID from orderPage does not match any in KDS.");
		}
		
		@AfterClass
		public void afterClass() {
			if(driverPos!=null) {
				driver=driverPos;
			try {
				orderPage.openKebabMenu();
				orderPage.voidTicket();
				orderPage.waitForTables();
				}
			catch(Exception e) {
				
				}
			finally {
				driverPos.quit();
				}
			}
			if(driverKDS1!=null) {
				driverKDS1.quit();
			}
			if(driverKDS2!=null) {
				driverKDS2.quit();
			}
		}

	}
