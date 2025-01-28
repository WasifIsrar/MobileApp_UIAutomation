package tests;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import baseTest.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import pages.KioskCheckoutPage;
import pages.KioskLandingPage;
import pages.KioskMenuPage;
import pages.OrdershubPage;
import pages.PinPage;
import pages.TablePage;
import utils.FileUtility;

public class KioskPosTest extends BaseTest{
	public AndroidDriver driver1;
	public AndroidDriver driver2;
	String pin;
	KioskMenuPage kioskMenuPage;
	KioskCheckoutPage checkoutPage;
	TablePage tablePage;
	OrdershubPage ordershubPage;
	
	@BeforeMethod(alwaysRun=true)
	@Parameters({"udid1","udid2","pin"})
	public void startApps(String udid1,String udid2,String pin,Method method) {
		String env=FileUtility.readEnvironmentFromFile();
		this.pin=pin;
		UiAutomator2Options options1=new UiAutomator2Options();
		options1.setCapability("udid",udid1);
		options1.setApp(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"resources"+File.separator+"app-"+env+"-release.apk");
		options1.setCapability("autoGrantPermissions",true);
		options1.setSystemPort(8300);
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
		getLogger().info("Starting test: " + method.getName());
	}
	
	@Test
	public void verifyKioskOrderOnPos() {
		PinPage pinPage=new PinPage(driver2);
		tablePage=pinPage.enterPin(pin.toCharArray());
		KioskLandingPage kioskLandingPage=new KioskLandingPage(driver1);
		kioskMenuPage=kioskLandingPage.continueAsGuest();
		kioskMenuPage.isPageDisplayed();
		kioskMenuPage.addItem(0);
		kioskMenuPage.addToCart();
		kioskMenuPage.checkout();
		checkoutPage=kioskMenuPage.checkout();
		checkoutPage.payByCash();
		checkoutPage.waitForBalanceDue();
		double subtotal=checkoutPage.getSubtotalPaymentAmount();
		double tax=checkoutPage.getTaxAmount();
		double serviceCharges=checkoutPage.getServiceCharge();
		double totalDue=checkoutPage.getTotalDue();
		checkoutPage.tapProceed();
		checkoutPage.waitForPaymentCash();
		checkoutPage.tapProceed();
		checkoutPage.skip();
		ordershubPage=tablePage.gotoOrdershub();
		ordershubPage.openFirstEntry();
		Assert.assertEquals(ordershubPage.getSource(),"kiosk","Source Incorrect on POS: ");
		Assert.assertEquals(ordershubPage.getSubTotalAmount(),subtotal,"Subtotal Incorrect on POS: ");
		Assert.assertEquals(ordershubPage.getTax(),tax,"Tax Incorrect on POS: ");
		Assert.assertEquals(ordershubPage.getServiceCharges(),serviceCharges,"Service Charges Incorrect on POS: ");
		Assert.assertEquals(ordershubPage.getTotalAmount(),totalDue,"Service Charges Incorrect on POS: ");
	}
	
	@Test
	public void verifyKioskOrderWithModifierOnPos() {
		PinPage pinPage=new PinPage(driver2);
		tablePage=pinPage.enterPin(pin.toCharArray());
		KioskLandingPage kioskLandingPage=new KioskLandingPage(driver1);
		kioskMenuPage=kioskLandingPage.continueAsGuest();
		kioskMenuPage.isPageDisplayed();
		kioskMenuPage.addItemWithModifier();
		kioskMenuPage.customise();
		kioskMenuPage.addModifier();
		kioskMenuPage.addToCart();
		checkoutPage=kioskMenuPage.checkout();
		checkoutPage=kioskMenuPage.checkout();
		checkoutPage.payByCash();
		checkoutPage.waitForBalanceDue();
		double subtotal=checkoutPage.getSubtotalPaymentAmount();
		double tax=checkoutPage.getTaxAmount();
		double serviceCharges=checkoutPage.getServiceCharge();
		double totalDue=checkoutPage.getTotalDue();
		checkoutPage.tapProceed();
		checkoutPage.waitForPaymentCash();
		checkoutPage.tapProceed();
		checkoutPage.skip();
		ordershubPage=tablePage.gotoOrdershub();
		ordershubPage.openFirstEntry();
		Assert.assertEquals(ordershubPage.getSource(),"kiosk","Source Incorrect on POS: ");
		Assert.assertEquals(ordershubPage.getSubTotalAmount(),subtotal,"Subtotal Incorrect on POS: ");
		Assert.assertEquals(ordershubPage.getTax(),tax,"Tax Incorrect on POS: ");
		Assert.assertEquals(ordershubPage.getServiceCharges(),serviceCharges,"Service Charges Incorrect on POS: ");
		Assert.assertEquals(ordershubPage.getTotalAmount(),totalDue,"Service Charges Incorrect on POS: ");
	}
	
	@AfterMethod(alwaysRun=true)
	public void voidAndQuit(ITestResult result) {
		if(driver1!=null) {
			driver1.quit();
		}
		if(driver2!=null) {
			driver2.quit();
		}
		getLogger().info("Finished Test: " + result.getMethod().getMethodName());
	}
}
