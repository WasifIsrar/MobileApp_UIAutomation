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
import pages.KDS;
import pages.MposOrderPage;
import pages.MposPinPage;
import pages.MposTablePage;
import utils.FileUtility;

public class MposKdsTest extends BaseTest{
	
	public AndroidDriver driver1;
	public AndroidDriver driver2;
	String pin;
	KDS kds;
	MposTablePage mPosTablePage;
	MposOrderPage mPosOrderPage;
	String tableNum; 
	String ticketId;
	
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
	
	@Test(priority=-1)
	public void verifyNoteAdded() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosPage.allowApp();
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.continueHere();
		mPosTablePage.isTableVisible();
		kds.closeDialog();
		kds.selectFirstStation();
		kds.confirmStation();
		openTableAddItem();
		String note="Special";
		mPosOrderPage.openOrderKebabMenu();
		mPosOrderPage.clickAddNote();
		mPosOrderPage.enterNote(note);
		mPosOrderPage.saveNote();
		Assert.assertTrue(mPosOrderPage.stay(),"Activity Status not displayed: ");
		kds.getTicketNumber();
		Assert.assertTrue(kds.getTicketDetails().contains(note),"Note incorrect on KDS: ");
	}
	
	@Test
	public void sendToKDS() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		kds.confirmStation();
		openTableAddItem();
		mPosOrderPage.send();
		mPosTablePage.isTableVisible();
		mPosTablePage.openHamburgerMenu();
		String serverName=mPosTablePage.getServerName();
		mPosTablePage.clickFloorProgress();
		mPosOrderPage=mPosTablePage.openTable(tableNum);
		mPosOrderPage.collapseMenu();
		kds.getTicketNumber();
		Assert.assertTrue(kds.getTableNumber().contains(tableNum),"Table Number does not match: ");
		Assert.assertEquals(kds.getServerName(),serverName,"Server Name does not match: ");
	}
	
	@Test
	public void editQuantityAfterSent() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		kds.confirmStation();
		openTableAddItem();
		mPosOrderPage.stay();
		kds.getTicketNumber();
		mPosOrderPage.editItem();
		mPosOrderPage.continueEditing();
		mPosOrderPage.increaseQuantity();
		mPosOrderPage.doneItem();
		Assert.assertTrue(mPosOrderPage.waitForQuantity(),"Quantity Not Increased: ");
		Assert.assertEquals(kds.getQuantity(),'2',"Quantity not updated on KDS: ");
	}
	
	@Test
	public void editModifierAfterSent() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		kds.confirmStation();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.scrollToTop();
		mPosOrderPage.addItemWithModifier();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.editItem();
		mPosOrderPage.addModifier();
		mPosOrderPage.doneItem();
		mPosOrderPage.waitForModifierUpdate("Chick");
		ticketId=mPosOrderPage.getTicketId();
		kds.setTicketId(ticketId);
		mPosOrderPage.isItemAdded();
		tableNum=mPosOrderPage.getTableNumber();
		mPosOrderPage.stay();
		kds.getTicketNumber();
		mPosOrderPage.editItem();
		mPosOrderPage.continueEditing();
		mPosOrderPage.editModifier();
		mPosOrderPage.doneItem();
		mPosOrderPage.waitForModifierUpdate("Shrim");
		String updatedModifier=mPosOrderPage.getModifier();
		System.out.println("Expected Modifier: "+updatedModifier);
		Assert.assertTrue(updatedModifier.contains("Shrim"),"Modifier Not Updated on mPOS: ");
		kds.openTicket();
		Assert.assertTrue(kds.getModifier().contains("Shrim"),"Modifier incorrect on kds: ");
	}
	
	@Test
	public void holdOneItem() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		kds.confirmStation();
		openTableAddItem();
		mPosOrderPage.expandMenu();
		mPosOrderPage.addItem();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.waitForItems(1);
		mPosOrderPage.editItem();
		mPosOrderPage.hold();
		Assert.assertTrue(mPosTablePage.isTableVisible(),"Did not navigate to table layout: ");
		mPosOrderPage=mPosTablePage.openTable(tableNum);
		mPosOrderPage.collapseMenu();
		kds.getTicketNumber();
		Assert.assertEquals(kds.getTicketNumber(),ticketId,"Ticket not found  on KDS: ");
	}
	
	@Test
	public void voidItem() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		kds.confirmStation();
		openTableAddItem();
		mPosOrderPage.expandMenu();
		mPosOrderPage.addSecondItem();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.waitForItems(1);
		String firstItem=mPosOrderPage.getItemNames().get(0);
		System.out.println("First Item: "+firstItem);
		String secondItem=mPosOrderPage.getItemNames().get(1);
		System.out.println("Second Item: "+secondItem);
		mPosOrderPage.stay();
		kds.getTicketNumber();
		mPosOrderPage.openOrderKebabMenu();
		mPosOrderPage.voidOrderItem();
		mPosOrderPage.voidItem();
		kds.openTicket();
		kds.selectItem(firstItem);
		Assert.assertEquals(kds.getClickable(),"false");
		kds.selectItem(firstItem);
		kds.selectItem(secondItem);
		Assert.assertEquals(kds.getClickable(),"true");
	}
	
	@Test
	public void voidOrder() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		kds.confirmStation();
		openTableAddItem();
		mPosOrderPage.stay();
		kds.getTicketNumber();
		mPosOrderPage.openOrderKebabMenu();
		mPosOrderPage.voidOrderItem();
		mPosOrderPage.voidWholeOrder();
		mPosOrderPage.isTableVisible();
		Assert.assertTrue(kds.isDisappear());
	}
	
	private void openTableAddItem() {
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.addItem();
		mPosOrderPage.collapseMenu();
		ticketId=mPosOrderPage.getTicketId();
		kds.setTicketId(ticketId);
		mPosOrderPage.isItemAdded();
		tableNum=mPosOrderPage.getTableNumber();
	}
	
	@AfterMethod
	public void quitDriver(ITestResult result) {
		if(driver1!=null) {
			try {
				if(mPosOrderPage.isOnOrderPage()) {
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
			}
			if(driver2!=null) {
				driver2.quit();
			}
			getLogger().info("Finished Test: " + result.getMethod().getMethodName());
		}
	
	@Test
	public void callServer() {
	    MposPinPage mPosPage = new MposPinPage(driver1);
	    mPosTablePage = mPosPage.enterPin(pin.toCharArray());
	    mPosTablePage.isTableVisible();
	    kds.confirmStation();
	    openTableAddItem();
	    mPosOrderPage.stay();
	    String ticketNumber = kds.getTicketNumber();
	    kds.openTicket();
	    kds.tapCallServer();
	    String callServerText = mPosOrderPage.getCallServerText();
	    String ExpectedText = "Chef needs you in the kitchen!".replaceAll("\\s+", " ");
	    String ActualText = callServerText.replaceAll("\\s+", " ");
	    Assert.assertEquals(ActualText, ExpectedText, "Not Equal");
	}
	
	@Test
	public void menuItemWithSpecialRequest() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		kds.confirmStation();
		openTableAddItem();
		ticketId=mPosOrderPage.getTicketId();
		kds.setTicketId(ticketId);
		mPosOrderPage.editItem();
		mPosOrderPage.tapOnSpecialRequest();
		String specialRequestText = "Testing Special Request";
		mPosOrderPage.addSpecialRequest(specialRequestText);
		mPosOrderPage.tapOnDoneBtn();
		mPosOrderPage.doneItem();
		String specialRequest = mPosOrderPage.getSpecialRequest();
		System.out.println("Special Request: " + specialRequest);
		mPosOrderPage.stay();
		String ticketNumber = kds.getTicketNumber();
		kds.openTicket();
//		String kdsSpecialRequest = kds.getSpecialRequest();
//		System.out.println("KDS Special Request: " + kdsSpecialRequest);
		Assert.assertEquals(specialRequest, specialRequestText, "Special Request doesn't added");
	}

}
