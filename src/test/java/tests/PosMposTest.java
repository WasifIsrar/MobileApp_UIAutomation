package tests;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
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
import pages.MposPinPage;
import pages.MposTablePage;
import pages.PinPage;
import pages.TablePage;
import utils.FileUtility;

public class PosMposTest extends BaseTest{
	
	public AndroidDriver driver1;
	public AndroidDriver driver2;
	String mPosPin;
	
	@BeforeMethod(alwaysRun=true)
	@Parameters({"udid","udid2","mPosPin"})
	public void startApps(String udid,String udid2,Method method,String mPosPin) throws MalformedURLException {
		this.mPosPin=mPosPin;
		String env=FileUtility.readEnvironmentFromFile();
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
		getLogger().info("Starting test: " + method.getName());
	}
	
	@Test(priority=-1)
	public void login() {
		PinPage pinPage=new PinPage(driver1);
		TablePage tablePage=pinPage.enterPin(mPosPin.toCharArray());
		try {
			tablePage.verifyClockIn();
		}
		catch(Exception e) {
			
		}
		MposPinPage mPosPinPage=new MposPinPage(driver2);
		MposTablePage mPosTablePage=mPosPinPage.enterPin(mPosPin.toCharArray());
		mPosTablePage.continueHere();
		Assert.assertTrue(mPosTablePage.isTableVisible(),"Login Failed: ");	
	}
	
	/*
	 * Voids Ticket
	 * If or not exception occurs in voidTicket
	 * quit drivers
	 */	
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