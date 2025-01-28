package tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import basePage.BasePage;
import baseTest.MomBaseTest;
import io.appium.java_client.AppiumBy;
import pages.LoginPage;
import pages.MomEmployeePage;
import pages.MomLostAndFoundPage;
import pages.MorePage;
import pages.MyAccountPage;

public class MyAccountTest extends MomBaseTest{
	
	MyAccountPage myAccountPage;
	MorePage morepage;
	MomEmployeePage employeePage;
	MomLostAndFoundPage lostAndFound;

	@Test
	public void editFirstName()
	{
		navigateTillLogin();
		myAccountPage = momdashboardPage.clickAccBtn();
		myAccountPage.clickChangeName();
		String randomName = myAccountPage.generateRandomName();
		System.out.println(randomName);
		myAccountPage.enterFirstName(randomName);
		myAccountPage.clickSaveBtn();
		Assert.assertTrue(myAccountPage.isNameDisplayed(randomName), "The name is not displayed as expected!");
		System.out.println(myAccountPage.isNameDisplayed(randomName));
	}
	@Test
	public void editLastName()
	{
		navigateTillLogin();
		myAccountPage = momdashboardPage.clickAccBtn();
		myAccountPage.clickChangeName();
		String randomName = myAccountPage.generateRandomName();
		System.out.println(randomName);
		myAccountPage.enterLastName(randomName);
		myAccountPage.clickSaveBtn();
		Assert.assertTrue(myAccountPage.isNameDisplayed(randomName), "The name is not displayed as expected!");
	}

	@Test
	public void changePassword() {
		navigateTillLogin();
		myAccountPage = momdashboardPage.clickAccBtn();
		momdashboardPage.enterPassword();
		String newPassword=myAccountPage.generateSimplePassword();
		System.out.println(newPassword);
		myAccountPage.changePassword("Aio@123456");
		myAccountPage.clickPswdSaveBtn1();
		myAccountPage.clickBackButton();
		momdashboardPage.clickMoreBtn();
//		momdashboardPage.gotoLogin();
//		Assert.assertTrue(myAccountPage.isNameDisplayed(randomName), "The name is not displayed as expected!");
	}

	@Test
	public void activateAccount() {
		navigateTillLogin();
		morepage =momdashboardPage.clickMoreBtn() ;
		employeePage= morepage.clickemployeeManagementBtn();
		employeePage.plusButton();
		employeePage.enterFirstName("Atif");
		String FirstName = employeePage.getFirstName();
		Assert.assertEquals(FirstName, "Atif");
		employeePage.enterLastName("Ali");
		String LastName = employeePage.getLastName();
		Assert.assertEquals(LastName, "Ali");
		String generatedEmail = employeePage.enterEmail();
		String emailGet = employeePage.getEmail();
		employeePage.assignRole();
		employeePage.selectRole();
		employeePage.enterAmount(100);
		employeePage.btnSave();
		employeePage.waitForRoles();
		employeePage.btnSave();
		employeePage.waitForEmployeePage();
		employeePage.clickBackButton();
		employeePage.btnLogout();
		employeePage.btnActivate();
		employeePage.enterActivateEmail(generatedEmail);
		Assert.assertEquals(emailGet, generatedEmail);
		employeePage.nextButton();
		employeePage.enterOTP(1, 2, 3, 4, 5, 6);
		employeePage.enterActivatePassword("Aio@123456");
		employeePage.enterActivateConfirmPassword("Aio@123456");
		employeePage.activateAndLogin();
		Assert.assertTrue(employeePage.isPageDisplayed(),"Employee activated");
	}
	
	@Test(dependsOnMethods = {"inactivateActiveEmployee"})
	public void activateInactiveEmployee() {
		navigateTillLogin();
		morepage =momdashboardPage.clickMoreBtn() ;
		employeePage= morepage.clickemployeeManagementBtn();
		employeePage.clickInactiveButton();
		employeePage.clickUser();
		employeePage.clickDetailsButton();
		employeePage.clickActivateButton();
		employeePage.clickActivateButtonAgain();
		employeePage.clickActiveButton();
        boolean isDisplayed = employeePage.confirmVisibility();
		Assert.assertTrue(isDisplayed, "Activated Employee Successfully.");
	}
	@Test
	public void inactivateActiveEmployee() {
		navigateTillLogin();
		morepage =momdashboardPage.clickMoreBtn() ;
		employeePage= morepage.clickemployeeManagementBtn();
		employeePage.clickUser();
		employeePage.clickDetailsButton();
		employeePage.clickActivateInactiveButton();
		employeePage.selectReason();
		employeePage.clickInactiveButton();
		boolean isDisplayed = employeePage.confirmVisibility();
		Assert.assertTrue(isDisplayed, "Deactivated Employee Successfully.");
	}
	
	@Test
	public void lostAnItem() {
		navigateTillLogin();
		morepage =momdashboardPage.clickMoreBtn();
		lostAndFound=morepage.clicklostAndFoundBtn();
		lostAndFound.plusButton();
		lostAndFound.lostItem();
		lostAndFound.enterItemName();
		String itemName = lostAndFound.getItemName();
		lostAndFound.selectCategory();
		lostAndFound.enterCustomerName("Ali");
		String CustomerName = lostAndFound.getCustomerName();
		Assert.assertEquals(CustomerName, "Ali");
		lostAndFound.enteringEmail();
		lostAndFound.enteringEmail();
		lostAndFound.enterPhoneNumber();
		lostAndFound.getPhoneNumber();
		lostAndFound.enterPrimaryColour("red");
		String primary=lostAndFound.getPrimaryColour();
		Assert.assertEquals(primary, "red");
		lostAndFound.enterOtherColour("green");
		String other = lostAndFound.getOtherColour();
		Assert.assertEquals(other, "green");
		lostAndFound.scrollToService();
		lostAndFound.enterServiceArea("Kitchen");
		String area =lostAndFound.getServiceArea();
		Assert.assertEquals(area, "Kitchen");
		lostAndFound.enterDescription();
		lostAndFound.clickCreateButton();
		String item= lostAndFound.getLostItem();
		Assert.assertEquals(item, itemName,"did not match the expected item name");
	}
	
	@Test
	public void foundAnItem() {
		navigateTillLogin();
		morepage =momdashboardPage.clickMoreBtn() ;
		lostAndFound=morepage.clicklostAndFoundBtn();
		lostAndFound.plusButton();
		lostAndFound.foundItem();
		lostAndFound.enterItemName();
		String itemName = lostAndFound.getItemName();
		lostAndFound.selectCategory();
		lostAndFound.enterPrimaryColour("red");
		String primary=lostAndFound.getPrimaryColour();
		Assert.assertEquals(primary, "red");
		lostAndFound.enterOtherColour("green");
		String other = lostAndFound.getOtherColour();
		Assert.assertEquals(other, "green");
		lostAndFound.scrollToService();
		lostAndFound.enterServiceArea("Kitchen");
		String area = lostAndFound.getServiceArea();
		Assert.assertEquals(area, "Kitchen");
		lostAndFound.enterDescription();
		lostAndFound.clickCreateButton();
		String actualItem=lostAndFound.scrollTo(itemName,"android.widget.ScrollView").getText();
		Assert.assertEquals(actualItem, itemName,"did not match the expected item name");
	}
}