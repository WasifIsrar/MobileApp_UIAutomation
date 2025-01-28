package tests;

import baseTest.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.KDS;
import pages.KioskCheckoutPage;
import pages.KioskLandingPage;
import pages.KioskMenuPage;
import utils.FileUtility;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;

public class KioskKDSTest extends BaseTest {
    private AndroidDriver driver1;
    private AndroidDriver driver2;
    KioskMenuPage kioskMenuPage;
    KioskCheckoutPage checkoutPage;
    KDS kds;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"udid1", "udid2", "app"})
    public void startApps(String udid1, String udid2, Method method, String app)
    {
        String env = FileUtility.readEnvironmentFromFile();
        UiAutomator2Options options1 = new UiAutomator2Options();
        options1.setCapability("udid", udid1);
        options1.setCapability("noReset", true);
        options1.setAppPackage("aio.app.kiosk." + app);
        options1.setAppActivity("aio.app.kiosk.ui.main.TapToStartActivity");
        options1.setCapability("autoGrantPermissions",true);
        options1.setSystemPort(8300);
        try
        {
            driver1 = new AndroidDriver(new URL("http://localhost:4723"), options1);
        }
        catch (Exception e)
        {
            getLogger().error("Driver 1 is not started: {}", String.valueOf(e));
        }
        driver1.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        UiAutomator2Options options2 = new UiAutomator2Options();
        options2.setCapability("udid", udid2);
        options2.setApp(System.getProperty("user.dir")+ File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"resources"+File.separator+"app-"+env+"-release.apk");
        options2.setCapability("autoGrantPermissions",true);
        options2.setSystemPort(8500);
        try
        {
            driver2 = new AndroidDriver(new URL("http://localhost:4725"), options2);
        }
        catch (Exception e)
        {
            getLogger().error("Driver 2 is not started: {}", String.valueOf(e));
        }
        driver2.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        kds = new KDS(driver2);

        getLogger().info("Starting test: {}", method.getName());
    }

    @Test
    public void verifyKioskKDS() {
        kds.confirmStation();
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
        checkoutPage.tapProceedToPayment();
        checkoutPage.getReceiptText();
        checkoutPage.tapSkip();
        String orderNumber = checkoutPage.getOrderNumber();
        checkoutPage.tapNext();
        kds.setTicketId(orderNumber);
        kds.getTicketNumber();
        kds.openTicket();
        String orderNumberKDS = kds.getOrderNumber();
        Assert.assertEquals(orderNumber, orderNumberKDS, "Order is not send to KDS from KIOSK");
    }

    @Test
    public void verifyModifierOnKDS() {
        kds.confirmStation();
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
        checkoutPage = kioskMenuPage.updateCart();
        String modifierName = checkoutPage.getModifiers();
        kioskMenuPage.checkout();
        checkoutPage.tapPayByCard();
        checkoutPage.waitForGoback();
        checkoutPage.tapProceedToPayment();
        checkoutPage.getReceiptText();
        checkoutPage.tapSkip();
        String orderNumber = checkoutPage.getOrderNumber();
        checkoutPage.tapNext();
        kds.setTicketId(orderNumber);
        kds.getTicketNumber();
        kds.openTicket();
        String kdsModifier = kds.getModifier();
        boolean isModifierAdded = modifierName.contains(kdsModifier);
        Assert.assertTrue(isModifierAdded, "Modifier is not added to KDS");
    }

    @Test
    public void verifySpecialRequestOnKDS() {
        kds.confirmStation();
        KioskLandingPage kioskLandingPage = new KioskLandingPage(driver1);
        kioskLandingPage.tapFirstScreen();
        kioskMenuPage = kioskLandingPage.continueAsGuest();
        kioskMenuPage.isPageDisplayed();
        kioskMenuPage.addItem(0);
        kioskMenuPage.addToCart();
        checkoutPage = kioskMenuPage.checkout();
        checkoutPage.clickItem();
        kioskMenuPage.customise();
        String specialRequest = "Testing Special Request From KIOSK To KDS";
        kioskMenuPage.enterSpecialRequest(specialRequest);
        kioskMenuPage.closeKeyboard();
        checkoutPage = kioskMenuPage.updateCart();
        String actualSpecialRequest = checkoutPage.getModifiers();
        kioskMenuPage.checkout();
        checkoutPage.tapPayByCard();
        checkoutPage.waitForGoback();
        checkoutPage.tapProceedToPayment();
        checkoutPage.getReceiptText();
        checkoutPage.tapSkip();
        String orderNumber = checkoutPage.getOrderNumber();
        checkoutPage.tapNext();
        kds.setTicketId(orderNumber);
        kds.getTicketNumber();
        kds.openTicket();
        String specialRequestKDS = kds.getModifier();
        boolean isSpecialRequestAdded = actualSpecialRequest.contains(specialRequestKDS);
        Assert.assertTrue(isSpecialRequestAdded, "Special Request is not sent from KIOSK to KDS");
    }
}
