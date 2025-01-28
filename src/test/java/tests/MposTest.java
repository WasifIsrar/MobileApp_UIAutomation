package tests;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
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
import utils.FileUtility;

public class MposTest extends BaseTest{
	public AndroidDriver driver1;
	MposTablePage mPosTablePage;
	MposOrderPage mPosOrderPage;
	String pin;
	
	@BeforeMethod
	@Parameters({"udid","pin"})
	public void initializeDriver(String udid,String pin,Method method) {
		this.pin=pin;
		String env=FileUtility.readEnvironmentFromFile();
		UiAutomator2Options options=new UiAutomator2Options();
		options.setCapability("udid",udid);
		options.setApp(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"resources"+File.separator+"app-"+env+"-release.apk");
		options.setCapability("autoGrantPermissions",true);
		options.setSystemPort(8300);
		options.setNewCommandTimeout(Duration.ofSeconds(600));
		try {
		driver1=new AndroidDriver(new URL("http://localhost:4723"),options);
		}
		catch(Exception e) {
			getLogger().error("Driver1 Not Started "+e);
		}
		driver1.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		getLogger().info("Starting test: " + method.getName());
	}
	
	@Test(priority=-1)
	public void continueHere() {
		MposPinPage mPosPinPage=new MposPinPage(driver1);
		mPosPinPage.allowApp();
		mPosTablePage=mPosPinPage.enterPin(pin.toCharArray());
		mPosTablePage.continueHere();
		Assert.assertTrue(mPosTablePage.isTableVisible(),"Login Failed: ");	
	}
	
	@Test
	public void verifyAmounts() {
		addItem("5");
		Double subtotal=mPosOrderPage.getSubtotal();
		double expectedServiceCharge=roundHalfUp(subtotal*0.25);
		double serviceCharge=mPosOrderPage.getServiceCharges();
		Assert.assertEquals(serviceCharge, expectedServiceCharge,"Service Charge Incorrect: ");
		double serviceInclusive=roundHalfUp(Double.sum(subtotal, expectedServiceCharge));
		double serviceTax=calculateServiceTax(expectedServiceCharge);
		double subtotalTax=calculateTax(subtotal);
		double expectedTax=roundHalfUp(Double.sum(serviceTax, subtotalTax));
		double expectedTotal=roundHalfUp(Double.sum(serviceInclusive, expectedTax));
		Assert.assertEquals(mPosOrderPage.getTotal(), expectedTotal);
		payByCash();
	}
	
	@Test
	public void exemptTax() {
		addItem("5");
		Double total=mPosOrderPage.getTotal();
		Double tax=mPosOrderPage.getTax();
		double expectedtotal=roundHalfUp(total-tax);
		mPosOrderPage.openKebabMenu();
		mPosOrderPage.selectTaxExemption();
		mPosOrderPage.waitForBalanceDue();
		Assert.assertEquals(mPosOrderPage.getTax(), 0,"Tax Not Exempted");
		Assert.assertEquals(mPosOrderPage.getTotal(), expectedtotal,"Total Incorrect after tax exemption");
		mPosOrderPage.itemNameVisible();
		payByCash();
	}
	
	@Test
	public void employeeDiscount() {
		addItem("5");
		double subtotalAmount=mPosOrderPage.getSubtotal();
		System.out.println("Subtotal: "+ subtotalAmount);
		mPosOrderPage.selectDealsDiscount();
		mPosOrderPage.selectApplyDiscount();
		mPosOrderPage.applyEmployeeDiscount();
		mPosOrderPage.enterTenDiscount();
		mPosOrderPage.apply();
		double expectedDiscount=roundHalfUp(subtotalAmount*0.1);
		mPosOrderPage.tick();
		mPosOrderPage.waitForBalanceDue();
		System.out.println("Expected Discount: "+expectedDiscount);
		Assert.assertEquals(mPosOrderPage.getDiscountAmount(), expectedDiscount,"Discount Amount Incorrect: ");
		double expectedSubtotalAmount=roundHalfUp(subtotalAmount-expectedDiscount);
		System.out.println("Expected Subtotal: "+expectedSubtotalAmount);
		double expectedServiceCharge=expectedSubtotalAmount*0.25;
		System.out.println("Expected Service Charges: "+ expectedServiceCharge);
		double serviceInclusive=roundHalfUp(Double.sum(expectedSubtotalAmount, expectedServiceCharge));
		double serviceTax=calculateServiceTax(expectedServiceCharge);
		double subtotalTax=calculateTax(expectedSubtotalAmount);
		double expectedTax=roundHalfUp(Double.sum(serviceTax,subtotalTax));
		double expectedTotal=roundHalfUp(Double.sum(serviceInclusive, expectedTax));
		Assert.assertEquals(mPosOrderPage.getTotal(), expectedTotal,"Total Unequal After Discount: ");
		payByCash();
	}
	
	@Test
	public void openPercentageCheck() {
		addItem("5");
		double subtotalAmount=mPosOrderPage.getSubtotal();
		mPosOrderPage.selectDealsDiscount();
		mPosOrderPage.selectApplyDiscount();
		mPosOrderPage.applyOpenCheckPercentage();
		mPosOrderPage.enterTenDiscount();
		mPosOrderPage.apply();
		mPosOrderPage.tick();
		mPosOrderPage.waitForBalanceDue();
		double expectedDiscount=roundHalfUp(subtotalAmount*0.1);
		System.out.println("Expected Discount: "+expectedDiscount);
		double expectedSubtotalAmount=roundHalfUp(subtotalAmount-expectedDiscount);
		System.out.println("Expected Subtotal: "+expectedSubtotalAmount);
		double expectedServiceCharge=roundHalfUp(expectedSubtotalAmount*0.25);
		double serviceInclusive=roundHalfUp(Double.sum(expectedSubtotalAmount, expectedServiceCharge));
		double serviceTax=calculateServiceTax(expectedServiceCharge);
		double subtotalTax=calculateTax(expectedSubtotalAmount);
		double expectedTax=roundHalfUp(Double.sum(serviceTax,subtotalTax));
		double taxInclusive=roundHalfUp(Double.sum(serviceInclusive, expectedTax));
		Assert.assertEquals(mPosOrderPage.getTotal(), taxInclusive,"Total Unequal After Discount: ");
		mPosOrderPage.getDiscountAppliedMessage();
		payByCash();
	}
	
	@Test
	public void openDollarCheck() {
		addItem("5");
		double subtotalAmount=mPosOrderPage.getSubtotal();
		mPosOrderPage.selectDealsDiscount();
		mPosOrderPage.selectApplyDiscount();
		mPosOrderPage.applyOpenCheckDollar();
		mPosOrderPage.enterTwoDollarDiscount();
		mPosOrderPage.apply();
		mPosOrderPage.tick();
		mPosOrderPage.waitForBalanceDue();
		double expectedSubtotalAmount=roundHalfUp(subtotalAmount-2);
		System.out.println("Expected Subtotal: "+ expectedSubtotalAmount);
		double expectedServiceCharge=roundHalfUp(expectedSubtotalAmount*0.25);
		double serviceInclusive=roundHalfUp(Double.sum(expectedSubtotalAmount, expectedServiceCharge));
		double serviceTax=calculateServiceTax(expectedServiceCharge);
		double subtotalTax=calculateTax(expectedSubtotalAmount);
		double expectedTax=roundHalfUp(Double.sum(serviceTax, subtotalTax));
		double taxInclusive=roundHalfUp(Double.sum(serviceInclusive, expectedTax));
		Assert.assertEquals(mPosOrderPage.getTotal(),taxInclusive,"Total Unequal After Discount: ");
		mPosOrderPage.getDiscountAppliedMessage();
		payByCash();
	}
	
	@Test
	public void managerDiscount() {
		addItem("5");
		mPosOrderPage.itemNameVisible();
		mPosOrderPage.selectDealsDiscount();
		mPosOrderPage.selectApplyDiscount();
		mPosOrderPage.applyManagerDiscount();
		mPosOrderPage.tick();
		mPosOrderPage.waitForSubtotal();
		Assert.assertEquals(mPosOrderPage.getTotal(),0,"Total Unequal After Manager Discount: ");
		mPosOrderPage.getDiscountAppliedMessage();
		payByCash();
	}
	
	@Test
	public void partialPayment() {
		addItem("5");
		mPosOrderPage.splitTicket();
		double amountBefore=mPosOrderPage.getAmount();
		mPosOrderPage.tapPlus();
		double expectedAmountAfter=formatWithoutRoundDoubleToTwoDecimals(amountBefore/2);
		Assert.assertEquals(mPosOrderPage.getAmount(),expectedAmountAfter,"Incorrect Amount after splitting by ways: ");
		mPosOrderPage.done();
		Assert.assertEquals(mPosOrderPage.getPaymentAmount(),expectedAmountAfter,"Incorrect Amount after split done: ");
		mPosOrderPage.payByCash();
		Assert.assertEquals(mPosOrderPage.getTotalAmount(),expectedAmountAfter,"Incorrect Amount on Payment: ");
		mPosOrderPage.selectExactAmount();
		mPosOrderPage.tapCompletePayment();
		mPosOrderPage.waitForReceiptModal();
		mPosOrderPage.clickCancelButton();
		mPosOrderPage.noReceipt();
		mPosOrderPage.giveExcellentReview();
		mPosOrderPage.closeThanksScreen();
		mPosOrderPage.waitForBalanceDue();
		Assert.assertTrue(mPosOrderPage.isPartial(),"Ticket Status Incorrect: ");
		double expectedRemainingAmount=formatWithoutRoundDoubleToTwoDecimals(amountBefore-expectedAmountAfter);
		Assert.assertEquals(mPosOrderPage.getBalanceDue(), expectedRemainingAmount,"Remaining Amount Incorrect: ");
		Assert.assertEquals(mPosOrderPage.getPaymentAmount(), expectedRemainingAmount,"Payment Amount Incorrect: ");
		mPosOrderPage.payByCash();
		Assert.assertEquals(mPosOrderPage.getTotalAmount(),expectedRemainingAmount,"Total Amount Incorrect on Payment: ");
		mPosOrderPage.selectExactAmount();
		mPosOrderPage.tapCompletePayment();
		mPosOrderPage.waitForReceiptModal();
		mPosOrderPage.clickCancelButton();
		mPosOrderPage.noReceipt();
		mPosOrderPage.giveExcellentReview();
		mPosOrderPage.closeThanksScreen();
		mPosOrderPage.isTableVisible();
	}
	
	@Test
	public void splitByGuests() {
		addItem("4");
		double subtotal=mPosOrderPage.getSubtotal();
		mPosOrderPage.splitTicket();
		mPosOrderPage.tapOtherOptions();
		double total=mPosOrderPage.getSplitTotal().get(0);
		mPosOrderPage.splitByGuests();
		List<Double> splittedParts=splitItemPrice(total,4);
		double part1=splittedParts.get(0);
		double part2=splittedParts.get(1);
		double part3=splittedParts.get(2);
		double part4=splittedParts.get(3);
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(0),part1,"Amount Incorrect on First Ticket: ");
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(2),part2,"Amount Incorrect on Second Ticket: ");
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(1),part3,"Amount Incorrect on Third Ticket: ");
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(3),part4,"Amount Incorrect on Fourth Ticket: ");
		mPosOrderPage.tickSplit();
		mPosOrderPage.waitForOrderScreen();
		mPosOrderPage.waitForBalanceDue();
		int ticketId=Integer.parseInt(mPosOrderPage.getSplitTicketNumber());
		double subtotalAmount1=mPosOrderPage.getSubtotal();
		mPosOrderPage.moveRight();
		mPosOrderPage.waitForTicket(Integer.toString(ticketId+1));
		double subtotalAmount2=mPosOrderPage.getSubtotal();
		mPosOrderPage.moveRight();
		mPosOrderPage.waitForTicket(Integer.toString(ticketId+2));
		double subtotalAmount3=mPosOrderPage.getSubtotal();
		mPosOrderPage.moveRight();
		mPosOrderPage.waitForTicket(Integer.toString(ticketId+3));
		double subtotalAmount4=mPosOrderPage.getSubtotal();
		double expectedSubtotal=roundHalfUp(subtotalAmount1+subtotalAmount2+subtotalAmount3+subtotalAmount4);
		Assert.assertEquals(subtotal, expectedSubtotal,"Subtotal Incorrect After Split: ");
	}

	@Test
	public void splitItem() {
		addItem("4");
		double subtotal=mPosOrderPage.getSubtotal();
		mPosOrderPage.splitTicket();
		mPosOrderPage.tapOtherOptions();
		double total=mPosOrderPage.getSplitTotal().get(0);
		mPosOrderPage.splitItem();
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(0), total);
		mPosOrderPage.moveItem();
		List<Double> splittedParts=splitItemPrice(total,3);
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(0),splittedParts.get(0));
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(2),splittedParts.get(1));
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(1),splittedParts.get(2));
		mPosOrderPage.tickSplit();
		mPosOrderPage.waitForOrderScreen();
		mPosOrderPage.waitForBalanceDue();
		int ticketId=Integer.parseInt(mPosOrderPage.getSplitTicketNumber());
		double subtotalAmount1=mPosOrderPage.getSubtotal();
		mPosOrderPage.moveRight();
		mPosOrderPage.waitForTicket(Integer.toString(ticketId+1));
		double subtotalAmount2=mPosOrderPage.getSubtotal();
		mPosOrderPage.moveRight();
		mPosOrderPage.waitForTicket(Integer.toString(ticketId+2));
		double subtotalAmount3=mPosOrderPage.getSubtotal();
		double expectedSubtotal=roundHalfUp(subtotalAmount1+subtotalAmount2+subtotalAmount3);
		Assert.assertEquals(subtotal, expectedSubtotal,"Subtotal Incorrect After Split: ");
	}
	
	@Test
	public void addCustomItem() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.openOrderKebabMenu();
		mPosOrderPage.addCustomItem();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForBalanceDue();
		Assert.assertEquals(mPosOrderPage.getSubtotal(),10);
	}
	
	@Test
	public void splitCustomByGuests() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("4");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.openOrderKebabMenu();
		mPosOrderPage.addCustomItem();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForBalanceDue();
		mPosOrderPage.splitTicket();
		mPosOrderPage.tapOtherOptions();
		double total=mPosOrderPage.getSplitTotal().get(0);
		mPosOrderPage.splitByGuests();
		List<Double> splittedParts=splitItemPrice(total,4);
		double part1=splittedParts.get(0);
		double part2=splittedParts.get(1);
		double part3=splittedParts.get(2);
		double part4=splittedParts.get(3);
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(0),part1,"Amount Incorrect on First Ticket: ");
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(2),part2,"Amount Incorrect on Second Ticket: ");
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(1),part3,"Amount Incorrect on Third Ticket: ");
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(3),part4,"Amount Incorrect on Fourth Ticket: ");
		mPosOrderPage.tickSplit();
		mPosOrderPage.waitForOrderScreen();
		mPosOrderPage.waitForBalanceDue();
	}
	
	@Test
	public void splitCustomItem() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("4");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.openOrderKebabMenu();
		mPosOrderPage.addCustomItem();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForBalanceDue();
		double subtotal=mPosOrderPage.getSubtotal();
		mPosOrderPage.splitTicket();
		mPosOrderPage.tapOtherOptions();
		double total=mPosOrderPage.getSplitTotal().get(0);
		mPosOrderPage.splitItem();
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(0), total);
		mPosOrderPage.moveItem();
		List<Double> splittedParts=splitItemPrice(total,3);
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(0),splittedParts.get(0));
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(2),splittedParts.get(1));
		Assert.assertEquals(mPosOrderPage.getSplitTotal().get(1),splittedParts.get(2));
		mPosOrderPage.tickSplit();
		mPosOrderPage.waitForOrderScreen();
		mPosOrderPage.waitForBalanceDue();
		int ticketId=Integer.parseInt(mPosOrderPage.getSplitTicketNumber());
		double subtotalAmount1=mPosOrderPage.getSubtotal();
		mPosOrderPage.moveRight();
		mPosOrderPage.waitForTicket(Integer.toString(ticketId+1));
		double subtotalAmount2=mPosOrderPage.getSubtotal();
		mPosOrderPage.moveRight();
		mPosOrderPage.waitForTicket(Integer.toString(ticketId+2));
		double subtotalAmount3=mPosOrderPage.getSubtotal();
		double expectedSubtotal=roundHalfUp(subtotalAmount1+subtotalAmount2+subtotalAmount3);
		Assert.assertEquals(expectedSubtotal, subtotal,"Subtotal Incorrect After Split: ");
	}
	
	@Test
	public void partialCustomPayment() {
		addItem("5");
		double amountBefore=mPosOrderPage.getPaymentAmount();
		mPosOrderPage.addCustomAmount();
		double customAmount=mPosOrderPage.getPaymentAmount();
		System.out.println("Remaining: "+(amountBefore-customAmount));
		double expectedAmount=roundHalfUp(amountBefore-customAmount);
		mPosOrderPage.done();
		mPosOrderPage.payByCash();
		mPosOrderPage.selectExactAmount();
		mPosOrderPage.tapCompletePayment();
		mPosOrderPage.waitForReceiptModal();
		mPosOrderPage.clickCancelButton();
		mPosOrderPage.noReceipt();
		mPosOrderPage.giveExcellentReview();
		mPosOrderPage.closeThanksScreen();
		mPosOrderPage.waitForBalanceDue();
		Assert.assertEquals(mPosOrderPage.getBalanceDue(),expectedAmount,"Balance Due Incorrect After Split: ");
		Assert.assertEquals(mPosOrderPage.getPaymentAmount(),expectedAmount,"Remaining Amount Incorrect After Split: ");
		payByCash();
	}
	
	@Test
	public void cancelTicket() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.clickAroundTheWorld();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		String tableNum=mPosOrderPage.getTableNumber();
		mPosOrderPage.openOrderKebabMenu();
		mPosOrderPage.clickCancelTicket();
		mPosOrderPage.clickYesBtn();
		Assert.assertTrue(mPosTablePage.isTableVisible(),"Navigation to Table Failed After Cancel: ");
		mPosTablePage.openTable(tableNum);
		Assert.assertTrue(mPosTablePage.isGuestPopupDisplayed(),"Table Not emptied after Cancel: ");
	}
	
	
	@AfterMethod
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
			getLogger().info("Finished Test: " + result.getMethod().getMethodName());
		}
	}
	
	private double formatWithoutRoundDoubleToTwoDecimals(double value) {
		String amount=Double.toString(value);
		String[] parts = amount.split("\\.");
		String decimalPart=parts[1];
		if(decimalPart.length()>=2) {
			return Double.parseDouble(parts[0]+"."+decimalPart.substring(0,2));
		}
		else {
			return Double.parseDouble(parts[0]+"."+decimalPart+"0");
		}
	}
	
	
	private List<Double> splitItemPrice(double amount,int parts){
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
		List<Double> formattedResult=new ArrayList<Double>();
		for(double r:result) {
			formattedResult.add(roundHalfUp(r));
		}
		return formattedResult;
	}
	
	public void addItem(String guests) {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable(guests);
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.clickAroundTheWorld();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForPaymentScreen();
		mPosOrderPage.waitForBalanceDue();
	}
	
	private void payByCash() {
		mPosOrderPage.payByCash();
		mPosOrderPage.selectExactAmount();
		mPosOrderPage.tapCompletePayment();
		mPosOrderPage.waitForReceiptModal();
		mPosOrderPage.clickCancelButton();
		mPosOrderPage.noReceipt();
		mPosOrderPage.giveExcellentReview();
		mPosOrderPage.thanksMessageText();
		mPosOrderPage.closeThanksScreen();
		mPosOrderPage.isTableVisible();
	}
	
	@Test
	public void editServiceCharges() {
		addItem("5");
		mPosOrderPage.itemNameVisible();
		mPosOrderPage.openKebabMenu();
		mPosOrderPage.editServiceCharge();
		mPosOrderPage.uncheckAutoGratuity();
		mPosOrderPage.save();
		mPosOrderPage.waitForBalanceDue();
		double expectedServiceCharges=roundHalfUp(mPosOrderPage.getSubtotal()*0.05);
		System.out.println("Expected Service Charge: " + expectedServiceCharges);
		Assert.assertEquals(mPosOrderPage.getServiceCharges(), expectedServiceCharges,"Service Charges Incorrect After Unchecking: ");
		double serviceInclusive=Double.sum(mPosOrderPage.getSubtotal(), expectedServiceCharges);
		System.out.println("Service Inclusive: " + serviceInclusive);
		double serviceTax=calculateServiceTax(expectedServiceCharges);
		System.out.println("Service Tax: " + serviceTax);
		double subtotalTax=calculateTax(mPosOrderPage.getSubtotal());
		System.out.println("Subtotal Tax: " + subtotalTax);
		double expectedTax=roundHalfUp(Double.sum(serviceTax, subtotalTax));
		System.out.println("Expected Tax: " + expectedTax);
		double expectedTotal=roundHalfUp(Double.sum(serviceInclusive, expectedTax));
		System.out.println("Expected Total: " + expectedTotal);
		Assert.assertEquals(mPosOrderPage.getTax(),expectedTax,"Wrong Tax Amount: ");
		Assert.assertEquals(mPosOrderPage.getTotal(), expectedTotal,"Wrong Total Amount: ");
		mPosOrderPage.payByCash();
		mPosOrderPage.selectExactAmount();
		mPosOrderPage.tapCompletePayment();
		mPosOrderPage.clickCancelButton();
		mPosOrderPage.noReceipt();
		mPosOrderPage.closeThanksScreen();
	}
	
	@Test
	public void uncheckAllServiceCharges() {
		addItem("5");
		mPosOrderPage.itemNameVisible();
		mPosOrderPage.openKebabMenu();
		mPosOrderPage.editServiceCharge();
		mPosOrderPage.uncheckAllServiceCharges();
		mPosOrderPage.save();
		mPosOrderPage.waitForBalanceDue();
		Assert.assertEquals(mPosOrderPage.getServiceCharges(), 0,"Service Charges Incorrect After Unchecking: ");
		double subTotal = mPosOrderPage.getSubtotal();
		double subtotalTax=calculateTax(mPosOrderPage.getSubtotal());
		System.out.println("Subtotal Tax: " + subtotalTax);
		double expectedTotal=roundHalfUp(Double.sum(subTotal, subtotalTax));
		System.out.println("Expected Total: " + expectedTotal);
		Assert.assertEquals(mPosOrderPage.getTax(),subtotalTax,"Wrong Tax Amount: ");
		Assert.assertEquals(mPosOrderPage.getTotal(), expectedTotal,"Wrong Total Amount: ");
		mPosOrderPage.payByCash();
		mPosOrderPage.selectExactAmount();
		mPosOrderPage.tapCompletePayment();
		mPosOrderPage.clickCancelButton();
		mPosOrderPage.noReceipt();
		mPosOrderPage.closeThanksScreen();
	}
	
	@Test
	public void voidOrder() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.clickAroundTheWorld();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		String tableNumber = mPosOrderPage.getTableNumber();
		mPosOrderPage.stay();
		mPosOrderPage.openOrderKebabMenu();
		mPosOrderPage.voidOrderItem();
		mPosOrderPage.voidWholeOrder();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables1=mPosTablePage.getOccupiedTables();
		boolean isAvailable = occupiedTables1.contains(tableNumber);
		Assert.assertFalse(isAvailable, "The Table Is Available");
	}
  
	@Test
	public void increaseGuests() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("4");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.clickAroundTheWorld();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.tapGuestLogo();
		mPosOrderPage.tapEditGuestCount();
		mPosOrderPage.addGuest();
		mPosOrderPage.tapOnDoneButton();
		mPosOrderPage.waitForToastMessage(10);
		int numberOfGuests = mPosOrderPage.getGuestNumber();
		Assert.assertEquals(numberOfGuests, 5, "Guests are not equal");
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		double serviceCharges = mPosOrderPage.getServiceCharges();
		double subTotal = mPosOrderPage.getSubtotal();
		double calculatedServiceCharges = roundHalfUp(subTotal * 0.25);
		Assert.assertEquals(calculatedServiceCharges, serviceCharges, "Actual and Expected Charges are not equal");
	}
	
	@Test
	public void addCustomer() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.tapGuestLogo();
		mPosOrderPage.tapAddGuestInfo();
		mPosOrderPage.tapOnNewGuest();
		mPosOrderPage.enterFirstName();
		mPosOrderPage.enterLastName();
		mPosOrderPage.enterPhoneNumber();
		mPosOrderPage.navigateBack();
		mPosOrderPage.save();
		mPosOrderPage.waitForToastMessage(5);
		mPosOrderPage.tapGuestLogo();
		mPosOrderPage.tapAddGuestInfo();
		String guest = mPosOrderPage.getGuestName();
		List<String> guestsNames = mPosOrderPage.getGuestList();
		boolean isGuestPresent = guestsNames.contains(guest);
		Assert.assertTrue(isGuestPresent, "New Guest is not present in the guest list");
	}
	
	@Test(priority = 1)
	public void changeServer() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.openHamburgerMenu();
		String serverName = mPosTablePage.getServerName();
		System.out.println("-----server name: " + serverName);
		mPosTablePage.clickOnProgressIcon();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.openOrderKebabMenu();
		mPosOrderPage.tapChangeServer();
		List<String> updatedServerNames = mPosOrderPage.getServerNameList(serverName);
		mPosOrderPage.assignServer(updatedServerNames.get(0));
		String selectedServer = mPosOrderPage.getSelectedServerName();
		mPosOrderPage.clickOnChangeServerBtn();
		mPosOrderPage.openOrderKebabMenu();
		mPosOrderPage.tapChangeServer();
		String changedServer = mPosOrderPage.getChangedServer(); 
		Assert.assertEquals(selectedServer, changedServer, "Server is not changed");	
	}
	
	@Test
	public void lockScreen() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.openHamburgerMenu();
		mPosTablePage.tapOnLockScreen();
		boolean isPinPageDisplayed = mPosTablePage.isPinScreenDisplayed();
		Assert.assertTrue(isPinPageDisplayed, "Pin Page is not displayed");
	}

	@Test
	public void tenderPaymentServerWithTip() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.clickAroundTheWorld();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		String tableNumber = mPosOrderPage.getTableNumber();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForPaymentScreen();
		mPosOrderPage.waitForBalanceDue();
		mPosOrderPage.selectDealsDiscount();
		mPosOrderPage.tapOnPayByTender();
		mPosOrderPage.tapOnServerWithTip();
		boolean isManagerApproval = mPosOrderPage.isManagerApprovalPinScreen();
		Assert.assertTrue(isManagerApproval, "Manager Approval Pin Screen is not displayed");
		mPosOrderPage.enterManagerPin(pin.toCharArray());
		mPosOrderPage.tapCompletePayment();
		boolean isAddATipScreen = mPosOrderPage.isAddATipScreen();
		Assert.assertTrue(isAddATipScreen, "Add a tip screen is not displayed");
		mPosOrderPage.tapOnDoneButton();
		mPosOrderPage.clickCancelButton();
		mPosOrderPage.noReceipt();
		mPosOrderPage.closeThanksScreen();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables1 = mPosTablePage.getOccupiedTables();
		boolean isAvailable = occupiedTables1.contains(tableNumber);
		Assert.assertFalse(isAvailable, "The Table Is Available");
	}
	
	@Test
	public void applyServiceChargeManually() {
		addItem("5");
		mPosOrderPage.openKebabMenu();
		mPosOrderPage.editServiceCharge();
		mPosOrderPage.clickManualServiceCharge();
		mPosOrderPage.save();
		mPosOrderPage.waitForBalanceDue();
		double expectedServiceCharges=roundHalfUp(mPosOrderPage.getSubtotal()*0.35);
		System.out.println("Expected Service Charge: " + expectedServiceCharges);
		Assert.assertEquals(mPosOrderPage.getServiceCharges(), expectedServiceCharges,"Service Charges are Incorrect");
	}
	
	@Test
	public void tenderPaymentManagerWithoutTip() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.clickAroundTheWorld();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForPaymentScreen();
		mPosOrderPage.itemNameVisible();
		mPosOrderPage.selectDealsDiscount();
		mPosOrderPage.tapOnPayByTender();
		mPosOrderPage.tapOnManagerWithoutTip();
//		boolean isManagerApproval = mPosOrderPage.isManagerApprovalPinScreen();
//		Assert.assertTrue(isManagerApproval, "Manager Approval Pin Screen is not displayed");
//		mPosOrderPage.enterManagerPin(pin.toCharArray());
		mPosOrderPage.tapCompletePayment();
		mPosOrderPage.clickCancelButton();
		mPosOrderPage.noReceipt();
		mPosOrderPage.closeThanksScreen();
	}
	
	@Test
	public void menuSearch() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.clickSearchButton();
		mPosOrderPage.clickSearchBar();
		String searchItem = "Martini";
		mPosOrderPage.searchItem(searchItem);
		List<String> searchedItems = mPosOrderPage.getSearchItemsList();
		boolean isAvailable = searchedItems.stream().anyMatch(item -> item.contains(searchItem));
		Assert.assertTrue(isAvailable, "Searched Item is not available");
	}

	@Test
	public void verifyDiscountRemovalOnDeselect()
	{
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.clickAroundTheWorld();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForPaymentScreen();
		double subtotalAmount = mPosOrderPage.getSubtotal();
		mPosOrderPage.itemNameVisible();
		mPosOrderPage.selectDealsDiscount();
		mPosOrderPage.selectApplyDiscount();
		mPosOrderPage.applyEmployeeDiscount();
		mPosOrderPage.applyOpenCheckDollar();
		mPosOrderPage.enterTenDiscount();
		mPosOrderPage.apply();
		double expectedDiscount = roundHalfUp(subtotalAmount * 0.1);
		mPosOrderPage.tick();
		mPosOrderPage.waitForBalanceDue();
		Assert.assertEquals(mPosOrderPage.getDiscountAmount(), expectedDiscount,"Discount Amount Incorrect");
		mPosOrderPage.getDiscountAppliedMessage();
		mPosOrderPage.selectDealsDiscount();
		mPosOrderPage.selectApplyDiscount();
		mPosOrderPage.unselectEmployeeDiscount();
		mPosOrderPage.tick();
		mPosOrderPage.waitForBalanceDue();
		double discountAfterDeselect = mPosOrderPage.getDiscountAmount();
		Assert.assertEquals(discountAfterDeselect, 0.0, "Discount is not equal to zero");
	}

	@Test
	public void verifyCustomerNameInPaymentTab()
	{
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.tapGuestLogo();
		mPosOrderPage.tapAddGuestInfo();
		mPosOrderPage.tapOnNewGuest();
		String fname = mPosOrderPage.enterFirstName();
		String lname = mPosOrderPage.enterLastName();
		String fullName = fname + " " + lname;
		mPosOrderPage.enterPhoneNumber();
		mPosOrderPage.navigateBack();
		mPosOrderPage.save();
		mPosOrderPage.waitForToastMessage(5);
		mPosOrderPage.expandMenu();
		mPosOrderPage.clickAroundTheWorld();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForPaymentScreen();
		mPosOrderPage.splitTicket();
		mPosOrderPage.tapPlus();
		mPosOrderPage.done();
		mPosOrderPage.payByCash();
        mPosOrderPage.selectExactAmount();
        mPosOrderPage.tapCompletePayment();
        mPosOrderPage.clickCancelButton();
        mPosOrderPage.noReceipt();
        mPosOrderPage.closeThanksScreen();
		mPosOrderPage.tapPaymentTab();
		String customerName = mPosOrderPage.getCustomerName();
		System.out.println("Customer Name: " + customerName);
		Assert.assertEquals(customerName, fullName, "Customer Name is not displayed");
	}

	@Test
	public void verifyTableNumberInMyTables() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.clickAroundTheWorld();
		mPosOrderPage.addSecondItem();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		String tableNumber = mPosOrderPage.getTableNumber();
		String ticketNumber = mPosOrderPage.getTicketId();
		int itemsCount = mPosOrderPage.getItemsCount();
		double subTotal = mPosOrderPage.getSubtotalOnPay();
		mPosOrderPage.send();
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		mPosTablePage.openMyTable(tableNumber);
		mPosOrderPage.collapseMenu();
		String tableNumberAfter = mPosOrderPage.getTableNumber();
		String ticketNumberAfter = mPosOrderPage.getTicketId();
		int itemsCountAfter = mPosOrderPage.getItemsCount();
		double subTotalAfter = mPosOrderPage.getSubtotalOnPay();
		Assert.assertEquals(tableNumber, tableNumberAfter, "Table number is not same");
		Assert.assertEquals(ticketNumber, ticketNumberAfter, "Ticket number is not same");
		Assert.assertEquals(itemsCount, itemsCountAfter, "Items are not same");
		Assert.assertEquals(subTotal, subTotalAfter, "Subtotal is not same");
	}

	@Test
	public void itemLevelDiscountOpenPercent() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.clickAroundTheWorld();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForPaymentScreen();
		double subtotalAmount = mPosOrderPage.getSubtotal();
		mPosOrderPage.selectDealsDiscount();
		mPosOrderPage.selectApplyDiscount();
		mPosOrderPage.clickApplyOnItem();
		mPosOrderPage.itemToApplyDiscount();
		mPosOrderPage.clickOpenPercent();
		mPosOrderPage.enterTenDiscount();
		mPosOrderPage.apply();
		double expectedDiscount = roundHalfUp(subtotalAmount * 0.1);
		mPosOrderPage.tickDiscountApply();
		mPosOrderPage.waitForBalanceDue();
		Assert.assertEquals(mPosOrderPage.getDiscountAmount(), expectedDiscount,"Discount Amount Incorrect: ");
		double expectedSubtotalAmount = roundHalfUp(subtotalAmount - expectedDiscount);
		double expectedServiceCharge = expectedSubtotalAmount * 0.25;
		double serviceInclusive = roundHalfUp(Double.sum(expectedSubtotalAmount, expectedServiceCharge));
		double serviceTax = calculateServiceTax(expectedServiceCharge);
		double subtotalTax = calculateTax(expectedSubtotalAmount);
		double expectedTax = roundHalfUp(Double.sum(serviceTax,subtotalTax));
		double expectedTotal = roundHalfUp(Double.sum(serviceInclusive, expectedTax));
		Assert.assertEquals(mPosOrderPage.getTotal(), expectedTotal,"Total Unequal After Discount: ");
		payByCash();
	}

	@Test
	public void itemLevelDiscountAndIncreaseQuantity() {
		MposPinPage mPosPage = new MposPinPage(driver1);
		mPosTablePage = mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables = mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table = mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage = mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.clickAroundTheWorld();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForPaymentScreen();
		mPosOrderPage.selectDealsDiscount();
		mPosOrderPage.selectApplyDiscount();
		mPosOrderPage.clickApplyOnItem();
		mPosOrderPage.itemToApplyDiscount();
		mPosOrderPage.clickOpenPercent();
		mPosOrderPage.enterTenDiscount();
		mPosOrderPage.apply();
		mPosOrderPage.tickDiscountApply();
		mPosOrderPage.waitForBalanceDue();
		mPosOrderPage.clickBackBtn();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.itemToApplyDiscount();
		mPosOrderPage.continueEditing();
		mPosOrderPage.tapPlus();
		mPosOrderPage.doneItem();
		mPosOrderPage.pay();
		mPosOrderPage.waitForPaymentScreen();
		double subtotalAmount = mPosOrderPage.getSubtotal();
		double discountAmount = mPosOrderPage.getDiscountAmount();
		double expectedDiscount = roundHalfUp(subtotalAmount * 0.1);
		Assert.assertEquals(discountAmount, expectedDiscount, "Discount Amount Incorrect: ");
		double expectedSubtotalAmount = roundHalfUp(subtotalAmount - expectedDiscount);
		double expectedServiceCharge = expectedSubtotalAmount * 0.25;
		double serviceInclusive = roundHalfUp(Double.sum(expectedSubtotalAmount, expectedServiceCharge));
		double serviceTax = calculateServiceTax(expectedServiceCharge);
		double subtotalTax = calculateTax(expectedSubtotalAmount);
		double expectedTax = roundHalfUp(Double.sum(serviceTax, subtotalTax));
		double expectedTotal = roundHalfUp(Double.sum(serviceInclusive, expectedTax));
		Assert.assertEquals(mPosOrderPage.getTotal(), expectedTotal, "Total Unequal After Discount: ");
		payByCash();
	}

	@Test
	public void increaseQuantityAndApplyItemLevelDiscount() {
		MposPinPage mPosPage = new MposPinPage(driver1);
		mPosTablePage = mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables = mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table = mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage = mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.clickAroundTheWorld();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		mPosOrderPage.itemToApplyDiscount();
		mPosOrderPage.tapPlus();
		mPosOrderPage.doneItem();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForPaymentScreen();
		double subtotalAmount = mPosOrderPage.getSubtotal();
		mPosOrderPage.selectDealsDiscount();
		mPosOrderPage.selectApplyDiscount();
		mPosOrderPage.clickApplyOnItem();
		mPosOrderPage.itemToApplyDiscount();
		mPosOrderPage.clickOpenPercent();
		mPosOrderPage.enterTenDiscount();
		mPosOrderPage.apply();
		double expectedDiscount = roundHalfUp(subtotalAmount * 0.1);
		mPosOrderPage.tickDiscountApply();
		mPosOrderPage.waitForBalanceDue();
		Assert.assertEquals(mPosOrderPage.getDiscountAmount(), expectedDiscount,"Discount Amount Incorrect: ");
		double expectedSubtotalAmount = roundHalfUp(subtotalAmount - expectedDiscount);
		double expectedServiceCharge = expectedSubtotalAmount * 0.25;
		double serviceInclusive = roundHalfUp(Double.sum(expectedSubtotalAmount, expectedServiceCharge));
		double serviceTax = calculateServiceTax(expectedServiceCharge);
		double subtotalTax = calculateTax(expectedSubtotalAmount);
		double expectedTax = roundHalfUp(Double.sum(serviceTax,subtotalTax));
		double expectedTotal = roundHalfUp(Double.sum(serviceInclusive, expectedTax));
		Assert.assertEquals(mPosOrderPage.getTotal(), expectedTotal,"Total Unequal After Discount: ");
		payByCash();
	}

	@Test
	public void itemLevelDiscountWithPriceModifier() {
		MposPinPage mPosPage = new MposPinPage(driver1);
		mPosTablePage = mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables = mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table = mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage = mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.scrollToTop();
		mPosOrderPage.addItemWithModifier();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		mPosOrderPage.clickCaesarSalad();
		mPosOrderPage.addSaladAdOnModifier();
		mPosOrderPage.increaseModifierQuantity();
		mPosOrderPage.doneItem();
		boolean isModifierAdded = mPosOrderPage.isModifierAdded();
		Assert.assertTrue(isModifierAdded, "Modifier Is Not Added");
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForPaymentScreen();
		double subtotalAmount = mPosOrderPage.getSubtotal();
		mPosOrderPage.selectDealsDiscount();
		mPosOrderPage.selectApplyDiscount();
		mPosOrderPage.clickApplyOnItem();
		mPosOrderPage.clickCaesarSalad();
		mPosOrderPage.clickOpenPercent();
		mPosOrderPage.enterTenDiscount();
		mPosOrderPage.apply();
		double expectedDiscount = roundHalfUp(subtotalAmount * 0.1);
		mPosOrderPage.tickDiscountApply();
		mPosOrderPage.waitForBalanceDue();
		Assert.assertEquals(mPosOrderPage.getDiscountAmount(), expectedDiscount,"Discount Amount Incorrect: ");
		double expectedSubtotalAmount = roundHalfUp(subtotalAmount - expectedDiscount);
		double expectedServiceCharge = expectedSubtotalAmount * 0.25;
		double serviceInclusive = roundHalfUp(Double.sum(expectedSubtotalAmount, expectedServiceCharge));
		double serviceTax = calculateServiceTax(expectedServiceCharge);
		double subtotalTax = calculateTax(expectedSubtotalAmount);
		double expectedTax = roundHalfUp(Double.sum(serviceTax,subtotalTax));
		double expectedTotal = roundHalfUp(Double.sum(serviceInclusive, expectedTax));
		Assert.assertEquals(mPosOrderPage.getTotal(), expectedTotal,"Total Unequal After Discount: ");
		payByCash();
	}

	@Test
	public void itemLevelDiscountOpenDollar() {
		MposPinPage mPosPage=new MposPinPage(driver1);
		mPosTablePage=mPosPage.enterPin(pin.toCharArray());
		mPosTablePage.isTableVisible();
		mPosTablePage.gotoMyTables();
		List<String> occupiedTables=mPosTablePage.getOccupiedTables();
		mPosTablePage.gotoLayoutView();
		WebElement table=mPosTablePage.findEmptyTable(occupiedTables);
		mPosTablePage.openTable(table);
		mPosOrderPage=mPosTablePage.addGuestsInTable("5");
		mPosOrderPage.isOrderPageDisplayed();
		mPosOrderPage.clickAroundTheWorld();
		mPosOrderPage.collapseMenu();
		mPosOrderPage.isItemAdded();
		mPosOrderPage.pay();
		mPosOrderPage.proceed();
		mPosOrderPage.waitForPaymentScreen();
		double subtotalAmount = mPosOrderPage.getSubtotal();
		mPosOrderPage.selectDealsDiscount();
		mPosOrderPage.selectApplyDiscount();
		mPosOrderPage.clickApplyOnItem();
		mPosOrderPage.itemToApplyDiscount();
		mPosOrderPage.clickOpenDollar();
		mPosOrderPage.enterTenDiscount();
		mPosOrderPage.apply();
		mPosOrderPage.tickDiscountApply();
		mPosOrderPage.waitForBalanceDue();
		Assert.assertEquals(mPosOrderPage.getDiscountAmount(), 10.00,"Discount Amount Incorrect: ");
		double expectedSubtotalAmount = roundHalfUp(subtotalAmount - 10.00);
		double expectedServiceCharge = expectedSubtotalAmount * 0.25;
		double serviceInclusive = roundHalfUp(Double.sum(expectedSubtotalAmount, expectedServiceCharge));
		double serviceTax = calculateServiceTax(expectedServiceCharge);
		double subtotalTax = calculateTax(expectedSubtotalAmount);
		double expectedTax = roundHalfUp(Double.sum(serviceTax,subtotalTax));
		double expectedTotal = roundHalfUp(Double.sum(serviceInclusive, expectedTax));
		Assert.assertEquals(mPosOrderPage.getTotal(), expectedTotal,"Total Unequal After Discount: ");
		payByCash();
	}
}
