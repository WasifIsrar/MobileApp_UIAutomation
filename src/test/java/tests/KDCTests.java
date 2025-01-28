package tests;

import baseTest.BaseTest;
import baseTest.POSKDSBaseTest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import pages.KDC;
import pages.KDS;
import pages.OrderPage;
import pages.PinPage;
import pages.TablePage;
import utils.FileUtility;

import java.io.File;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.URL;


public class KDCTests extends BaseTest {
	public AndroidDriver driver1;
	public AndroidDriver driver2;
	public AndroidDriver driver3;
	String pin;
	KDS kds;
	KDC kdc;
	PinPage pinPage;
	TablePage tablePage;
	OrderPage orderPage;
	
	@BeforeMethod(alwaysRun=true)
	@Parameters({"udid", "udid1", "udid2", "pin"})
	public void startApp(String udid, String udid1, String udid2, Method method, String pin) {
		String env = FileUtility.readEnvironmentFromFile();
		this.pin = pin;
		//POS
		UiAutomator2Options options = new UiAutomator2Options();
		options.setCapability("udid", udid);
		options.setApp(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"resources"+File.separator+"app-"+env+"-release.apk");
		options.setCapability("autoGrantPermissions", true);
		options.setSystemPort(8300);
		try 
		{
			driver1 = new AndroidDriver(new URL("http://localhost:4723"), options);
		}
		catch(Exception e) 
		{
			getLogger().error("Driver 1 is not started" + e);
		}
		driver1.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		//KDS
		UiAutomator2Options options1 = new UiAutomator2Options();
		options1.setCapability("udid", udid1);
		options1.setApp(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"resources"+File.separator+"app-"+env+"-release.apk");
		options1.setCapability("autoGrantPermissions", true);
		options1.setSystemPort(8500);
		options1.setNewCommandTimeout(Duration.ofSeconds(600));
		try
		{
			driver2 = new AndroidDriver(new URL("http://localhost:4725"), options1);
		}
		catch(Exception e)
		{
			getLogger().error("Driver 2 is not started" + e);
		}
		driver2.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		kds = new KDS(driver2);
		
		//KDC
		UiAutomator2Options options2 = new UiAutomator2Options();
		options2.setCapability("udid", udid2);
		options2.setApp(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"resources"+File.separator+"app-"+env+"-release.apk");
		options2.setCapability("autoGrantPermissions", true);
		options2.setSystemPort(8700);
		options2.setNewCommandTimeout(Duration.ofSeconds(600));
		try
		{
			driver3 = new AndroidDriver(new URL("http://localhost:4727"), options2);
		}
		catch(Exception e)
		{
			getLogger().error("Driver 3 is not started" + e);
		}
		driver2.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		kdc = new KDC(driver3);
		
		getLogger().info("Starting test: " + method.getName());
	}
	
	
	@Test
	public void checkSentTicketOnKDC() {
		pinPage = new PinPage(driver1);
		tablePage = pinPage.enterPin(pin.toCharArray());
		tablePage.isTableDisplayed();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("5");
		orderPage.isPageDisplayed();
		orderPage.addItem();
		orderPage.stayHere();
		String ticketId = orderPage.getTicketId();
		System.out.println("----Ticket ID: " + ticketId);
		kds.confirmStation();
		kds.setTicketId(ticketId);
		kds.getTicketNumber();
		kds.openTicket();
		String orderNumber = kds.getOrderNumber();
		System.out.println("------ KDS Order Number-----: " + orderNumber);
		List<String> kdcOrderNumbers = kdc.getPreparingOrderNumbers(orderNumber);
		System.out.println("----- KDC Order Number-----: " + kdcOrderNumbers);
		Assert.assertTrue(kdcOrderNumbers.contains(orderNumber), "Order is not in preparing tab");
	}
	
	@Test
	public void checkFulfilledTicketOnKDC() {
		pinPage = new PinPage(driver1);
		tablePage = pinPage.enterPin(pin.toCharArray());
		tablePage.isTableVisible();
		kds.closeDialog();
		kds.selectFirstStation();
		kds.confirmStation();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("5");
		orderPage.isPageDisplayed();
		orderPage.addItem();
		orderPage.stayHere();
		String ticketId = orderPage.getTicketId();
		System.out.println("----Ticket ID: " + ticketId);
		kds.setTicketId(ticketId);
		kds.getTicketNumber();
		kds.openTicket();
		String orderNumber = kds.getOrderNumber();
		System.out.println("------ KDS Order Number-----: " + orderNumber);
		kds.selectAll();
		kds.fulfill();
		List<String> kdcOrderNumbers = kdc.getReadyOrderNumbers(orderNumber);
		System.out.println("----- KDC Order Number-----: " + kdcOrderNumbers);
		Assert.assertTrue(kdcOrderNumbers.contains(orderNumber), "Order is not in ready tab");
	}
	
}
