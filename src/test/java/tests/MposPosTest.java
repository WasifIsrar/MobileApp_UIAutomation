package tests;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import baseTest.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import pages.MposOrderPage;
import pages.MposPinPage;
import pages.MposTablePage;
import pages.OrdershubPage;
import pages.PinPage;
import pages.TablePage;
import utils.FileUtility;

public class MposPosTest extends BaseTest{
	
	String posPin;
	String mposPin;
	public AndroidDriver driver1;
	public AndroidDriver driver2;
	MposOrderPage mPosOrderPage;
	
	
	@BeforeMethod(alwaysRun=true)
	@Parameters({"udid","udid2","posPin","mposPin"})
	public void startApps(String udid,String udid2,Method method,String posPin,String mposPin) throws MalformedURLException {
		String env=FileUtility.readEnvironmentFromFile();
		this.posPin=posPin;
		this.mposPin=mposPin;
		UiAutomator2Options options1=new UiAutomator2Options();
		options1.setCapability("udid",udid);
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
	public void verifyMposOrderOnPos() {
		PinPage posPinPage=new PinPage(driver2);
		TablePage posTablePage=posPinPage.enterPin(posPin.toCharArray());
		MposPinPage mPosPage=new MposPinPage(driver1);
		MposTablePage mPosTablePage=mPosPage.enterPin(mposPin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		MposOrderPage mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.addItem();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		String tableNum=mPosOrderPage.getTableNumber();
		String ticketId=mPosOrderPage.getTicketId();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForPaymentScreen();
		mPosOrderPage.waitForBalanceDue();
		double serviceCharges=mPosOrderPage.getServiceCharges();
		double subtotal=mPosOrderPage.getSubtotal();
		double tax=mPosOrderPage.getTax();
		double total=mPosOrderPage.getTotal();
		mPosOrderPage.payByCash();
		mPosOrderPage.selectExactAmount();
		mPosOrderPage.tapCompletePayment();
		mPosOrderPage.waitForReceiptModal();
		mPosOrderPage.noReceipt();
		mPosOrderPage.giveExcellentReview();
		mPosOrderPage.closeThanksScreen();
		mPosOrderPage.isTableVisible();
		OrdershubPage ordershubPage=posTablePage.gotoOrdershub();
		ordershubPage.openEntry(ticketId);
		Assert.assertEquals(ordershubPage.getSubTotalAmount(),subtotal,"Subtotal Unequal on Ordershub: ");
		Assert.assertEquals(ordershubPage.getTax(),tax,"Tax Unequal on Ordershub: ");
		Assert.assertEquals(ordershubPage.getServiceCharges(),serviceCharges,"Service Charges Unequal on Ordershub: ");
		Assert.assertEquals(ordershubPage.getTotalAmount(),total,"Total Unequal on Ordershub: ");
		Assert.assertEquals(ordershubPage.getTableNumber(),tableNum,"Table Number Incorrect on Ordershub: ");
	}
	
	@Test
	public void verifyPartialPaidOrder() {
		PinPage posPinPage=new PinPage(driver2);
		TablePage posTablePage=posPinPage.enterPin(posPin.toCharArray());
		MposPinPage mPosPage=new MposPinPage(driver1);
		MposTablePage mPosTablePage=mPosPage.enterPin(mposPin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		MposOrderPage mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.addItem();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		String ticketId=mPosOrderPage.getTicketId();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForPaymentScreen();
		mPosOrderPage.waitForBalanceDue();
		mPosOrderPage.splitTicket();
		mPosOrderPage.tapPlus();
		mPosOrderPage.done();
		mPosOrderPage.payByCash();
		mPosOrderPage.selectExactAmount();
		mPosOrderPage.tapCompletePayment();
		mPosOrderPage.waitForReceiptModal();
		mPosOrderPage.noReceipt();
		mPosOrderPage.giveExcellentReview();
		mPosOrderPage.closeThanksScreen();
		mPosOrderPage.waitForBalanceDue();
		double balanceDue=mPosOrderPage.getBalanceDue();
		OrdershubPage ordershubPage=posTablePage.gotoOrdershub();
		ordershubPage.openEntry(ticketId);
		Assert.assertEquals(ordershubPage.getStatus(ticketId),"partial","Ticket Status Incorrect on Ordershub: ");
		Assert.assertEquals(ordershubPage.getBalanceDue(),balanceDue,"Balance Due Incorrect on Ordershub: ");
	}
	
	@AfterMethod(alwaysRun=true)
	public void quitDriver(ITestResult result) {
		if(driver1!=null) {
			try {
				if(mPosOrderPage.isOnOrderPage()||mPosOrderPage.isOnPaymentPage()) {
					if(mPosOrderPage.isOnPaymentPage()) {
						mPosOrderPage.goBack();
						mPosOrderPage.isOrderPageDisplayed();
						mPosOrderPage.collapseMenu();
					}
					mPosOrderPage.openOrderKebabMenu();
					mPosOrderPage.voidOrderItem();
					mPosOrderPage.voidWholeOrder();
					mPosOrderPage.isTableVisible();
				}
			}				
			catch(Exception e) {
			}
			finally {
				driver1.quit();
			}
			if(driver2!=null) {
				driver2.quit();
			}
			getLogger().info("Finished Test: " + result.getMethod().getMethodName());
		}
	}

}
