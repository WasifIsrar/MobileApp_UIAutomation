package pages;

import java.time.Duration;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class MyAccountPage extends BasePage{

	public MyAccountPage(AndroidDriver driver) {
		super(driver);
	
	}
	By fNameInput = AppiumBy.accessibilityId("textInput-First Name");
	By lNameInput = AppiumBy.accessibilityId("textInput-Last Name");
	By passChange = AppiumBy.accessibilityId("textInput-Current Password");
	By addPhone = AppiumBy.accessibilityId("Add Phone Number");
	By multiToggle = AppiumBy.accessibilityId("switchBtn");
	By viewPin = By.xpath("//android.view.ViewGroup[@content-desc=\"eye\"]/com.horcrux.svg.SvgView/com.horcrux.svg.GroupView/com.horcrux.svg.PathView");
	By reNewPin = By.xpath("//android.view.ViewGroup[@content-desc=\"reGenerate\"]/com.horcrux.svg.SvgView/com.horcrux.svg.GroupView/com.horcrux.svg.PathView");
	By imgBtn = AppiumBy.accessibilityId("editImage");
	By galleryBtn = AppiumBy.accessibilityId("gallery");
	By cameraBtn = AppiumBy.accessibilityId("camera");
	By removePhotoBtn = AppiumBy.accessibilityId("Remove Photo");
	By cropbtn = AppiumBy.accessibilityId("Crop");
	By cancelBtn = AppiumBy.accessibilityId("Navigate up");
	By camclickBtn = By.id("com.transsion.camera:id/shutter_button");
	By camClkDoneBtn = By.id("com.transsion.camera:id/done_button");
	By camClkCancelBtn = By.id("com.transsion.camera:id/intent_action_cancel");
	By passChngeBtn = By.xpath("(//android.view.ViewGroup[@content-desc=\"Change\"])[1]");
	By phoneChange = By.xpath("(//android.widget.TextView[@text=\"Change\"])[2]");
	By crntPswdField = AppiumBy.accessibilityId("textInput-Current Password");
	By clickPassword = AppiumBy.accessibilityId("Current Password");
	By newPswdField = AppiumBy.accessibilityId("textInput-New Password");
	By reEntrNewPswdField = AppiumBy.accessibilityId("textInput-Re-enter Password");
	By pswdSavebtn = AppiumBy.accessibilityId("Save");
	By closeBtn = AppiumBy.accessibilityId("closeButton");
	By phnField = By.className("android.widget.EditText");
	By nxtBtn = AppiumBy.accessibilityId("Next");
	By swtchBtn = AppiumBy.accessibilityId("switchBtn");
	By uploadBtn = By.xpath("//android.view.ViewGroup[@content-desc=\'Uploaded\']");
	By addBtn = AppiumBy.accessibilityId("addMoreBtn");
	By docs = By.xpath("(//android.widget.LinearLayout[@resource-id=\"com.google.android.documentsui:id/item_root\"])[1]/android.widget.LinearLayout");
	By downDoc = AppiumBy.accessibilityId("downloadDoc");
	By deleteDoc = AppiumBy.accessibilityId("deleteDoc");
	By backBtn = AppiumBy.accessibilityId("Go Back");
	By PswdChangeDate = AppiumBy.accessibilityId("lastChanged");
	By clickName = By.xpath("//android.view.ViewGroup[@content-desc=\'Changed Name\']");
	By FirstName = By.xpath("//android.widget.TextView[@text=\'Testing er\']");
	
	
	
	public void clickSaveBtn() {
		waitForElementVisibility(saveBtn,10);
		driver.findElement(saveBtn).click();
		
	}
    public void clickChangeName() {
		driver.findElement(clickName).click();
	}
	
	public void enterFirstName(String fName) {
		
		driver.findElement(fNameInput).clear();
		driver.findElement(fNameInput).sendKeys(fName);
	}
	public String generateRandomName() {
        String[] firstNames = {"Alice", "Bob", "Charlie", "Diana", "Edward", "Fiona", "George", "Hannah"};
        Random random = new Random();
        return firstNames[random.nextInt(firstNames.length)] + " " + (char) (random.nextInt(26) + 'A');
    }
	public boolean isNameDisplayed(String expectedName) {
		String dynamicXpath = "(//android.widget.TextView[contains(@text, \"" + expectedName + "\")])";
		 return driver.findElement(By.xpath(dynamicXpath)).isDisplayed();
	}
	public void enterLastName(String lName) {
		driver.findElement(lNameInput).clear();
		driver.findElement(lNameInput).sendKeys(lName);
	}
	
	public String getFirstName() {
		String firstName = driver.findElement(fNameInput).getText();
		return firstName;
	}
	
	public String getLastName() {
		String lastName = driver.findElement(lNameInput).getText();
		return lastName;
	}
//	public void clickBackButton() {
//		waitForElementPresent(backBtn,10);
//		driver.findElement(backBtn).click();
//	}
	
	public void changePassword(String currentPass) {
		String pass1 = generateSimplePassword(); // Generate new password
		driver.findElement(passChange).click();
		driver.findElement(crntPswdField).sendKeys(currentPass);
		driver.findElement(newPswdField).sendKeys(pass1);
		driver.findElement(clickPassword).click();
		driver.findElement(reEntrNewPswdField).sendKeys(pass1);

	}
	public String generateSimplePassword() {
		String lettersAndNumbers = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String specialCharacters = "!@#$%^&*";
        String allCharacters = lettersAndNumbers + specialCharacters;
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));
        for (int i = 1; i < 8; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }
        return password.toString();
    }
	public void clickPswdSaveBtn1() {
		driver.findElement(pswdSavebtn).click();
	}
}
	
	