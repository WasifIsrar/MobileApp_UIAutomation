package baseTest;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import basePage.BasePage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import pages.LoginPage;
import pages.MomDashboardPage;
import pages.MomEmployeePage;
import pages.MorePage;

public class MomBaseTest extends BaseTest{
	
	public AndroidDriver driver1;
	public AppiumDriverLocalService service;
	public MorePage morePage;
	public MomDashboardPage momdashboardPage;
	public MomEmployeePage EmployeePage;
	String email;
	String loginPassword;
	
	@BeforeMethod
	@Parameters({"environment", "udid","email","loginPassword"})
	public void beforeMethod(String environment, String udid,String email,String loginPassword) throws MalformedURLException
	{	
		this.email=email;
		this.loginPassword=loginPassword;
		UiAutomator2Options options = new UiAutomator2Options();
		options.setCapability("udid", udid);
		options.setCapability("autoGrantPermissions", true);
		options.setApp(System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"java"+File.separator+"resources"+File.separator+"app-"+ environment +"-release.apk");
		driver1 = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
		driver1.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	}
	
	public void navigateTillLogin() {
		BasePage basePage = new BasePage(driver1);
		basePage.isMomPageDisplayed();
		LoginPage loginPage = basePage.gotoLogin();
		momdashboardPage =  loginPage.verifyLogin(email,loginPassword);
	}
	
	@AfterMethod
	public void afterMethod() {
		try {
			driver1.quit();
		}
		catch (Exception e) {
			
		}
	}

}
