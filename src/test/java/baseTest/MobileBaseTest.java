package baseTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import basePage.OnlineBasePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import pages.OrderingQRPage;

public class MobileBaseTest {

	static Properties pro = new Properties();
    public static String validPhone = getProperty("userPhone");
    public static String validPass = getProperty("userPass");
    public static String invalidPhone = getProperty("invalidPhone");
    public static String incompletePhone = getProperty("incompletePhone");
    public static String validCard = getProperty("validCard");
    public static String invalidCard = getProperty("invalidCard");
    public static String failCard = getProperty("failCard");
    public static String validExpiry = getProperty("validExpiry");
    public static String invalidExpiry = getProperty("invalidExpiry");
    public static String validCVC = getProperty("validCVC");
    public static String invalidCVC = getProperty("invalidCVC");
    public static String cardCountry = getProperty("countryCard");
    public static String testEmail = getProperty("testMail");
    public static String userFirstName = getProperty("firstName");
    public static String userLastName = getProperty("lastName");
    public static String userRandomNumber = getProperty("randomNumber");
    public static String userInvalidName = getProperty("invalidName");
    public static String location = getProperty("restLocation");
    public static String expectedLoc = getProperty("expectedLocation");
    //    public static String allergy = getProperty("Allergen");
    public static List<String> allergy = Collections.singletonList(pro.getProperty("Allergen"));
    private static final Properties properties = new Properties();
    public OrderingQRPage OrderingQRPage;


    public String platform;
    public String baseUrl;

    DesiredCapabilities cap = new DesiredCapabilities();
    AppiumDriver driver;
    WebDriver webDriver;
    UiAutomator2Options options = new UiAutomator2Options();
    OrderingQRPage e;
	@BeforeMethod
    @Parameters({"baseUrl", "browser", "device", "platform"})
	public void setup(String baseUrl,String browser,String device,String platform) throws MalformedURLException {
		DesiredCapabilities cap = new DesiredCapabilities();
        if (device.equalsIgnoreCase("IOS")) {
            cap.setCapability("platformName", "ios");
            cap.setCapability("browserName", "Safari");
            cap.setCapability("appium:udid", "9C04061D-F8D6-4D9A-BE1B-724B8CD3FA58");
            cap.setCapability("appium:automationName", "xcuitest");
            cap.setCapability("appium:platformVersion", "17.2");
            cap.setCapability("nativeWebTap", true);
            driver = new IOSDriver(new URL("http://127.0.0.1:4723"), cap);

        } else if(device.equalsIgnoreCase("Android")) {
            cap.setCapability("platformName", "Android");
            cap.setCapability("browserName", "Chrome");
            cap.setCapability("appium:udid", "emulator-5554");
            cap.setCapability("appium:automationName", "UiAutomator2");
            cap.setCapability("appium:chromedriverAutoDownload", true);
            cap.setCapability("appium:chromedriverExecutable", getDriverPath());
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), cap);

        } else {
            throw new IllegalArgumentException("Unsupported OS for mobile testing: " + device);
        }
        OrderingQRPage=new OrderingQRPage();
        OnlineBasePage.setDriver(driver);
        driver.get(baseUrl);
	}

	private String getDriverPath() {
		String chromeDriverPath = System.getProperty("webdriver.chrome.driver");
	    System.out.println("ChromeDriver Path: " + chromeDriverPath);
	    return chromeDriverPath;
	}
	
	// Retrieve a property value by key
    public static String getProperty(String key) {
//        return properties.getProperty(key);

        String value = properties.getProperty( key);
        if (value == null) {
            System.out.println("Warning: Key '" + key + "' not found in properties file.");
        }
        return value;
    }
}
