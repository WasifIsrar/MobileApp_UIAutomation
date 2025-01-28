package tests;


import baseTest.MobileBaseTest;

import org.testng.Assert;
import org.testng.annotations.Test;

public class OrderingQRTest extends MobileBaseTest {

    @Test(description = "Verify Happy validation in Sign up Form")
    public void verifyHappyValidationCheck() throws InterruptedException {
        OrderingQRPage.enterFirstName(userFirstName);
        OrderingQRPage.enterLastName(userLastName);
        OrderingQRPage.enterPhoneNumber(userRandomNumber);
        OrderingQRPage.enterEmail(testEmail);
        Assert.assertTrue(OrderingQRPage.checkedBoxAssert(),"Check box is not Checked");
        Assert.assertTrue(OrderingQRPage.startBtnAssert(),"Start Button is not Enabled");
        OrderingQRPage.clickStartOrderingBtn();
        OrderingQRPage.waitForElementToBeVisible(OrderingQRPage.mainMenuAssert);
        Assert.assertEquals(OrderingQRPage.mainMenuAssert(),"Roboonline","Drinks Menu Not Found");

    }


    @Test(description = "Verify negative validation in Sign up Form")
    public void verifyNegativeValidationCheck(){
        OrderingQRPage.enterFirstName(userInvalidName);
        OrderingQRPage.enterLastName(userInvalidName);

        String firstNameAssert= OrderingQRPage.firstNameAssert();
        Assert.assertEquals(firstNameAssert,"Incorrect name format","Format is valid");

        OrderingQRPage.enterFirstName(userFirstName);

        String lastNameAssert= OrderingQRPage.lastNameAssert();
        Assert.assertEquals(lastNameAssert,"Incorrect name format","Format is valid");

        OrderingQRPage.enterLastName(userLastName);
        OrderingQRPage.enterPhoneNumber(userRandomNumber);
        OrderingQRPage.enterEmail(testEmail);
        OrderingQRPage.clickLoginLink();

        boolean loginAssert= OrderingQRPage.loginLinkAssert();
        Assert.assertTrue(loginAssert,"Login Page not Found");

    }
}

