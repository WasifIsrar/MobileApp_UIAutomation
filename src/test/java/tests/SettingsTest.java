package tests;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import baseTest.POSBaseTest;

public class SettingsTest extends POSBaseTest{
	String areaName;
	String tableNum;
	
	
	
	@Test(groups= {"Smoke"})
	public void renamePrinter() {
		enterPin();
		isTableScreenDisplayed();
		settingsPage=tablePage.gotoSettings();
		Assert.assertTrue(settingsPage.verifyDeviceRenamed("Printer", "NewPrinterName"), "Fail to rename printer");
	}
	
	@Test(groups= {"Smoke"})
	public void renameKDS() {
		enterPin();
		isTableScreenDisplayed();
		settingsPage=tablePage.gotoSettings();
		Assert.assertTrue(settingsPage.verifyDeviceRenamed("KDS", "Auto KDS"), "Fail to rename kds");
	}
	
	@Test(groups= {"Smoke"})
	public void createArea() {
		enterPin();
		isTableScreenDisplayed();
		settingsPage=tablePage.gotoSettings();
		settingsPage.gotoTableLayout();
		areaName=generateRandomString(3);
		settingsPage.verifyAreaAdded(areaName);
		settingsPage.gotoTableServe();
		Assert.assertTrue(tablePage.verifyFloorAdded(areaName),"Floor not Added to Table Serve: ");
	}
	
	@Test(groups= {"Smoke"},dependsOnMethods= {"createArea"})
	public void editLayout() {
		enterPin();
		isTableScreenDisplayed();
		settingsPage=tablePage.gotoSettings();
		settingsPage.gotoTableLayout();
		settingsPage.selectArea(areaName);
		settingsPage.editLayout();
		tableNum=generateRandomString(2);
		settingsPage.addTableToArea(tableNum);
		settingsPage.saveChanges();
		settingsPage.gotoTableServe();
		tablePage.gotoFloor(areaName);
		Assert.assertTrue(tablePage.verifyTablePresent(tableNum),"Table not added to Floor: ");
	}
	
	@Test(groups= {"Smoke"},dependsOnMethods= {"editLayout"})
	public void deleteTable() {
		enterPin();
		isTableScreenDisplayed();
		settingsPage=tablePage.gotoSettings();
		settingsPage.gotoTableLayout();
		settingsPage.selectArea(areaName);
		settingsPage.editLayout();
		settingsPage.deleteTable(tableNum);
		settingsPage.saveChanges();
		settingsPage.gotoTableServe();
		tablePage.gotoFloor(areaName);
		Assert.assertNotEquals(tablePage.getTableNumbers(),tableNum,"Table not deleted from Floor: ");
	}
	
	@Test(groups= {"Smoke"},dependsOnMethods= {"deleteTable"})
	public void deleteArea() {
		enterPin();
		isTableScreenDisplayed();
		settingsPage=tablePage.gotoSettings();
		settingsPage.gotoTableLayout();
		settingsPage.selectArea(areaName);
		Assert.assertTrue(settingsPage.verifyAreaDeleted().contains("Successfully"),"Cofirmation message incorrect on deleting area: ");
		settingsPage.gotoTableServe();
		Assert.assertTrue(tablePage.isAreaDeleted(areaName),"Area not deleted from table screen: ");
	}
	
	
	public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; // Can include numbers if desired
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }
        
        return randomString.toString();
	}
}
