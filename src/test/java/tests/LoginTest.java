package tests;
import org.testng.Assert;
import org.testng.annotations.Test;
import baseTest.MomBaseTest;


public class LoginTest extends MomBaseTest{
	
	@Test
	public void login()
	{
		navigateTillLogin();
		Assert.assertTrue(momdashboardPage.isScreenDisplay());
	}

}
