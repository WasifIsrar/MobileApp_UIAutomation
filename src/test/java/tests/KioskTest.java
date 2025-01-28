package tests;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import baseTest.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import pages.KioskCartPage;
import pages.KioskCheckoutPage;
import pages.KioskLandingPage;
import pages.KioskMenuPage;
import utils.FileUtility;

public class KioskTest extends BaseTest{
	
	public AndroidDriver driver1;
	KioskMenuPage kioskMenuPage;
	KioskCheckoutPage checkoutPage;
	
	@BeforeMethod(alwaysRun=true)
	@Parameters({"udid", "app"})
	public void startApp(String udid, Method method, String app) {
		String env = FileUtility.readEnvironmentFromFile();
		UiAutomator2Options options = new UiAutomator2Options();
		options.setCapability("udid", udid);
		options.setAppPackage("aio.app.kiosk." + app);
		options.setAppActivity("aio.app.kiosk.ui.main.TapToStartActivity");
		//options.setApp(...); // Optional: set the APK path if needed
		options.setCapability("autoGrantPermissions", true);
		options.setSystemPort(8300);
		options.setCapability("noReset", true);

		try {
			if (driver1 == null) {
				driver1 = new AndroidDriver(new URL("http://localhost:4723"), options);
			}
		} catch (Exception e) {
			getLogger().error("Driver Not Started: " + e);
		}
		driver1.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		getLogger().info("Starting test: " + method.getName());
	}
	
	@Test(priority=1)
	public void verifyPrice() {
		KioskLandingPage kioskLandingPage = new KioskLandingPage(driver1);
		kioskLandingPage.tapFirstScreen();
		kioskMenuPage = kioskLandingPage.continueAsGuest();
		kioskMenuPage.isPageDisplayed();
		kioskMenuPage.addItem(0);
		//kioskMenuPage.waitForCustomizeButton();
		double itemPrice = Double.parseDouble(kioskMenuPage.getItemPrice());
		System.out.println("-----Item Price: " + itemPrice);
		kioskMenuPage.addToCart();
		checkoutPage = kioskMenuPage.checkout();
		Assert.assertEquals(checkoutPage.getSubtotal(), itemPrice, "Wrong Subtotal on Checkout: ");
		checkoutPage.tapCancelBtn();
		checkoutPage.tapCancelOrderBtn();
	}
	
	@Test(priority=2)
	public void verifySameItem() {
		KioskLandingPage kioskLandingPage = new KioskLandingPage(driver1);
		kioskLandingPage.tapFirstScreen();
		kioskMenuPage = kioskLandingPage.continueAsGuest();
		kioskMenuPage.isPageDisplayed();
		kioskMenuPage.addItem(0);
		kioskMenuPage.addToCart();
		KioskCartPage kioskCartPage = kioskMenuPage.viewCart();
		double subtotal = Double.parseDouble(kioskCartPage.getSubtotal());
		kioskCartPage.goBack();
		kioskMenuPage.addItem(0);
		kioskMenuPage.addToCart();
		checkoutPage = kioskMenuPage.checkout();
		double expectedSubtotal = subtotal * 2;
		Assert.assertEquals(checkoutPage.getSubtotal(), expectedSubtotal, "Wrong Subtotal after 2 same items: ");
		checkoutPage.tapCancelBtn();
		checkoutPage.tapCancelOrderBtn();
	}
	
	@Test(priority=3)
	public void increaseQuantity() {
		KioskLandingPage kioskLandingPage = new KioskLandingPage(driver1);
		kioskLandingPage.tapFirstScreen();
		kioskMenuPage = kioskLandingPage.continueAsGuest();
		kioskMenuPage.isPageDisplayed();
		kioskMenuPage.addItem(0);
		kioskMenuPage.addToCart();
		checkoutPage = kioskMenuPage.checkout();
		double price = checkoutPage.getPrice();
		double subtotal = checkoutPage.getSubtotal();
		checkoutPage.addQuantity();
		double expectedSubtotal = roundHalfUp(subtotal * 2);
		Assert.assertEquals(checkoutPage.getPrice(), roundHalfUp(price * 2), "Item Price Incorrect After Increasing Quantity: ");
		Assert.assertEquals(checkoutPage.getSubtotal(), expectedSubtotal, "Subtotal Incorrect After Increasing Quantity: ");
		checkoutPage.tapCancelBtn();
		checkoutPage.tapCancelOrderBtn();
	}
	
	@Test(priority=4)
	public void updateModifier() {
		KioskLandingPage kioskLandingPage = new KioskLandingPage(driver1);
		kioskLandingPage.tapFirstScreen();
		kioskMenuPage = kioskLandingPage.continueAsGuest();
		kioskMenuPage.isPageDisplayed();
		kioskMenuPage.addItemWithModifier();
		kioskMenuPage.addToCart();
		checkoutPage = kioskMenuPage.checkout();
		checkoutPage.clickItem();
		kioskMenuPage.customise();
		kioskMenuPage.waitForClearChanges();
		kioskMenuPage.tapSaladAddOn();
		kioskMenuPage.addModifier();
		kioskMenuPage.updateCart();
		checkoutPage.clickItem();
		kioskMenuPage.customise();
		kioskMenuPage.tapSaladAddOn();
		String expectedModifier = kioskMenuPage.getModifiersName().get(1);
		System.out.println("-----Expected Modifier Name: " + expectedModifier);
		kioskMenuPage.removeModifier();
		kioskMenuPage.changeModifier();
		checkoutPage = kioskMenuPage.updateCart();
		String actualModifier = checkoutPage.getModifiers();
		System.out.println("-----Actual Modifier Name: " + actualModifier);
		boolean isModifierUpdated = actualModifier.contains(expectedModifier);
		Assert.assertTrue(isModifierUpdated, "Modifier Incorrect After Updating: ");
		checkoutPage.tapCancelBtn();
		checkoutPage.tapCancelOrderBtn();
	}
	
	@Test(priority=5)
	public void addMoreItems() {
		KioskLandingPage kioskLandingPage = new KioskLandingPage(driver1);
		kioskLandingPage.tapFirstScreen();
		kioskMenuPage = kioskLandingPage.continueAsGuest();
		kioskMenuPage.isPageDisplayed();
		kioskMenuPage.addItem(0);
		kioskMenuPage.addToCart();
		checkoutPage = kioskMenuPage.checkout();
		double subtotalBefore = checkoutPage.getSubtotal();
		checkoutPage.addMore();
		kioskMenuPage.addItem(1);
		double secondItemPrice = kioskMenuPage.getItemTotalPrice();
		kioskMenuPage.addToCart();
		kioskMenuPage.checkout();
		double actualSubtotal = checkoutPage.getSubtotal();
		double expectedSubtotal = roundHalfUp(Double.sum(subtotalBefore, secondItemPrice));
		Assert.assertEquals(actualSubtotal, expectedSubtotal, "Subtotal Incorrect After Adding Another Item: ");
		Assert.assertEquals(checkoutPage.itemCount(), 2, "Item Count Incorrect After Adding Another Item: ");
		checkoutPage.tapCancelBtn();
		checkoutPage.tapCancelOrderBtn();
	}
	
	@Test(priority = 6)
	public void menuSearch() {
		KioskLandingPage kioskLandingPage = new KioskLandingPage(driver1);
		kioskLandingPage.tapFirstScreen();
		kioskMenuPage = kioskLandingPage.continueAsGuest();
		kioskMenuPage.isPageDisplayed();
		kioskMenuPage.clickSignatureFood();
		kioskMenuPage.clickSearch();
		String searchItem = "Martini";
		kioskMenuPage.searchItem(searchItem);
		List<String> searchedItemsList = kioskMenuPage.getSearchedItems();
		boolean isAvailable = searchedItemsList.stream().anyMatch(item -> item.contains(searchItem));
		Assert.assertTrue(isAvailable, "Searched Item is not available");
		kioskMenuPage.closeSearchPopUp();
		kioskMenuPage.startNew();
	}
	
	@Test(priority = 7)
	public void startNewOrder() {
		KioskLandingPage kioskLandingPage = new KioskLandingPage(driver1);
		kioskLandingPage.tapFirstScreen();
		kioskMenuPage = kioskLandingPage.continueAsGuest();
		kioskMenuPage.isPageDisplayed();
		kioskMenuPage.addItem(0);
		kioskMenuPage.addToCart();
		kioskMenuPage.startNew();
		kioskLandingPage.tapFirstScreen();
		kioskMenuPage = kioskLandingPage.continueAsGuest();
		kioskMenuPage.isPageDisplayed();
		int size = kioskMenuPage.getCartButtonSize();
		Assert.assertEquals(size, 0, "Size of cart button is not equal to zero");
		kioskMenuPage.startNew();
	}
	
	@Test(priority = 8)
	public void verifyAmountsWithTip() {
		KioskLandingPage kioskLandingPage = new KioskLandingPage(driver1);
		kioskLandingPage.tapFirstScreen();
		kioskMenuPage = kioskLandingPage.continueAsGuest();
		kioskMenuPage.isPageDisplayed();
		kioskMenuPage.addItem(0);
		kioskMenuPage.addToCart();
		checkoutPage = kioskMenuPage.checkout();
		checkoutPage.checkout();
		checkoutPage.tapPayByCard();
		checkoutPage.waitForGoback();
		double subtotalAmount = checkoutPage.getSubtotalPaymentAmount();
		System.out.println("----Subtotal----: " + subtotalAmount);
		double expectedServiceCharges = roundHalfUp(subtotalAmount * 0.05);
		Assert.assertEquals(checkoutPage.getServiceCharge(), expectedServiceCharges, "Service Charges are not correct");
		double expectedTip = roundHalfUp(subtotalAmount * 0.20);
		System.out.println("----Tip----: " + expectedTip);
		Assert.assertEquals(checkoutPage.getTip(), expectedTip, "Tip is incorrect");
		double serviceTax = calculateServiceTax(expectedServiceCharges);
		double subtotalTax = calculateTax(subtotalAmount);
		double expectedTotalTax = Double.sum(serviceTax, subtotalTax);
		System.out.println("---Total Tax---: " + expectedTotalTax);
		Assert.assertEquals(checkoutPage.getTaxAmount(), expectedTotalTax, "Tax is not correct");
		double expectedTotalDue = roundHalfUp(subtotalAmount + expectedTotalTax + expectedTip + expectedServiceCharges);
		System.out.println("---Total Due---: " + expectedTotalDue);
		Assert.assertEquals(checkoutPage.getTotalDue(), expectedTotalDue, "Total Due is not correct");
		checkoutPage.tapProceedToPayment();
		checkoutPage.getReceiptText();
		checkoutPage.tapSkip();
		checkoutPage.tapNext();
	}
	
	@Test(priority = 9)
	public void verifyAmountsWithoutTip() {
		KioskLandingPage kioskLandingPage = new KioskLandingPage(driver1);
		kioskLandingPage.tapFirstScreen();
		kioskMenuPage = kioskLandingPage.continueAsGuest();
		kioskMenuPage.isPageDisplayed();
		kioskMenuPage.addItem(0);
		kioskMenuPage.addToCart();
		checkoutPage = kioskMenuPage.checkout();
		checkoutPage.checkout();
		checkoutPage.tapPayByCard();
		checkoutPage.waitForGoback();
		checkoutPage.tapNoTip();
		double subtotalAmount = checkoutPage.getSubtotalPaymentAmount();
		System.out.println("----Subtotal----: " + subtotalAmount);
		double expectedServiceCharges = roundHalfUp(subtotalAmount * 0.05);
		Assert.assertEquals(checkoutPage.getServiceCharge(), expectedServiceCharges, "Service Charges are not correct");
//		double expectedTip = roundHalfUp(subtotalAmount * 0.20);
//		System.out.println("----Tip----: " + expectedTip);
		Assert.assertEquals(checkoutPage.getTip(), 0.0, "Tip is incorrect");
		double serviceTax = calculateServiceTax(expectedServiceCharges);
		double subtotalTax = calculateTax(subtotalAmount);
		double expectedTotalTax = Double.sum(serviceTax, subtotalTax);
		System.out.println("---Total Tax---: " + expectedTotalTax);
		Assert.assertEquals(checkoutPage.getTaxAmount(), expectedTotalTax, "Tax is not correct");
		double expectedTotalDue = roundHalfUp(subtotalAmount + expectedTotalTax + expectedServiceCharges);
		System.out.println("---Total Due---: " + expectedTotalDue);
		Assert.assertEquals(checkoutPage.getTotalDue(), expectedTotalDue, "Total Due is not correct");
		checkoutPage.tapProceedToPayment();
		checkoutPage.getReceiptText();
		checkoutPage.tapSkip();
		checkoutPage.tapNext();
	}
	
	@Test(priority = 10)
	public void verifyItemWithSpecialRequest() {
		KioskLandingPage kioskLandingPage = new KioskLandingPage(driver1);
		kioskLandingPage.tapFirstScreen();
		kioskMenuPage = kioskLandingPage.continueAsGuest();
		kioskMenuPage.isPageDisplayed();
		kioskMenuPage.addItem(0);
		kioskMenuPage.addToCart();
		checkoutPage = kioskMenuPage.checkout();
		checkoutPage.clickItem();
		kioskMenuPage.customise();
		String specialRequest = "Testing Special Request";
		kioskMenuPage.enterSpecialRequest(specialRequest);
		kioskMenuPage.closeKeyboard();
		checkoutPage = kioskMenuPage.updateCart();
		String actualSpecialRequest = checkoutPage.getModifiers();
		System.out.println("----Actual Special Request----: " + actualSpecialRequest);
		boolean isSpecialRequestAdded = actualSpecialRequest.contains(specialRequest);
		Assert.assertTrue(isSpecialRequestAdded, "Special Request is not added");
	}
	

	@AfterClass(alwaysRun = true)
	public void quit() {
		if (driver1 != null) {
			driver1.quit();
			getLogger().info("All tests finished. Driver quit.");
		}
	}
}
