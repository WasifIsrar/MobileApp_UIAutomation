package pages;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import basePage.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class MomEmployeePage extends BasePage{
	
	public MomEmployeePage(AndroidDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	By addBtn = AppiumBy.accessibilityId("addMoreBtn");
//	By EmployeeManagementBtn = AppiumBy.accessibilityId("Employee Management");
	By fNameInput = AppiumBy.accessibilityId("textInput-First Name");
	By lNameInput = AppiumBy.accessibilityId("textInput-Last Name");
	By emailField =AppiumBy.accessibilityId("textInput-Email Address");
	By AssignRole =AppiumBy.accessibilityId("Assign roles");
	By SelectRole = AppiumBy.accessibilityId("Select a role");
	By EnterServer = By.xpath("//android.view.ViewGroup[@content-desc=\'Server\']");
	By EnterAmount = By.xpath("//android.widget.EditText[@text=\'$0 /Hour\']");
	By SaveButton = AppiumBy.accessibilityId("Save");
	By EmployeeText =By.xpath("//android.widget.TextView[@text='Employees']");
	By LogoutBtn = AppiumBy.className("Logout");
	By ActivateBtn = AppiumBy.accessibilityId("Activate account");
	By NextButton = AppiumBy.accessibilityId("Next");
	By ActivateEmail =AppiumBy.accessibilityId("textInput-Email address");
	By OTPOne = AppiumBy.accessibilityId("OTP digit 1");
	By OTPTwo = AppiumBy.accessibilityId("OTP digit 2");
	By OTPThree = AppiumBy.accessibilityId("OTP digit 3");
	By OTPFour = AppiumBy.accessibilityId("OTP digit 4");
	By OTPFive = AppiumBy.accessibilityId("OTP digit 5");
	By OTPSix = AppiumBy.accessibilityId("OTP digit 6");
	By ActivatePassword= AppiumBy.accessibilityId("textInput-Password");
	By ActivateConfirmPassword= AppiumBy.accessibilityId("textInput-Confirm password");
	By ActivateAndLogin = AppiumBy.accessibilityId("Activate & Log In");
	By BackBtn = By.className("com.horcrux.svg.PathView");
	By MoreTabs =AppiumBy.accessibilityId("More");
	
	//Active an Employee
	By btnActive = AppiumBy.accessibilityId("Active");
	By btnDeactivate =AppiumBy.accessibilityId("Deactivate");
	By reasonSelect = AppiumBy.accessibilityId("Select a reason");
	By btnDeactivateAgain = By.xpath("(//android.widget.TextView[@text=\'Deactivate\'])[2]");
	By textVisibility = By.xpath("//android.widget.TextView[@text=\'Deactivate Account\']");
	
	//Employee Page Activate an Inactive Employee
	By btnInactive =AppiumBy.accessibilityId("Inactive");
	By userClick = By.xpath("(//android.widget.TextView[@text=\'Server\'])[1]");
	By btnDetails =AppiumBy.accessibilityId("Details");
	By btnActivate =AppiumBy.accessibilityId("Reactivate");
	By btnActivateAgain = By.xpath("(//android.widget.TextView[@text=\'Reactivate\'])[2]");
	By visibleServer = By.xpath("(//android.widget.TextView[@text=\'Server\'])[1]");
	
	By PageDisplayed = AppiumBy.accessibilityId("Server");
	//Lost and Found 
	

	//plus button
		public void plusButton() {
			driver.findElement(addBtn).click();
		}
		public void enterFirstName(String fName) {
			
			driver.findElement(fNameInput).clear();
			driver.findElement(fNameInput).sendKeys(fName);
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

	
	public void assignRole() {
		driver.findElement(AssignRole).click();
		driver.findElement(SelectRole).click();
		
	}
	public void selectRole() {
		WebElement serverElement = scrollTo("Server","android.widget.ScrollView");
        serverElement.click();
	}
	
	
	public int enterAmount(int amount) {
		driver.findElement(EnterAmount).click();
		driver.findElement(EnterAmount).sendKeys(String.valueOf(amount));
		return amount;
	}
	public void btnSave() {
		driver.findElement(SaveButton).click();
		
	}
	public void waitForRoles() {
		driver.findElement(AssignRole).isDisplayed();
		
	}
	public void waitForEmployeePage() {
		waitForElementVisibility(EmployeeText,5);
	}
	
//	public void load() {
////		Thread.sleep(7000);
////		 WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
////		    driverWait.until(ExpectedConditions.elementToBeClickable(MoreBtn));
//		WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
//		driverWait.until(driver -> false);
//	}

	public void btnLogout() {
		WebElement serverEle = scrollTo("Logout","android.widget.ScrollView");
        serverEle.click();
	}
	public void btnActivate() {
		driver.findElement(ActivateBtn).click();
	}
	public void enterActivateEmail(String eEmail) {
		driver.findElement(ActivateEmail).clear();
		driver.findElement(ActivateEmail).sendKeys(eEmail);
	}

	public String getActivateEmail() {
		String eEmail =driver.findElement(ActivateEmail).getText();
		return eEmail;
	}
	public void nextButton() {
		driver.findElement(NextButton).click();
	}
	public void enterOTP(int one, int two, int three, int four, int five, int six) {
	    
	    driver.findElement(OTPOne).click();
	    driver.findElement(OTPOne).sendKeys(String.valueOf(one));
	    
	    driver.findElement(OTPTwo).click();
	    driver.findElement(OTPTwo).sendKeys(String.valueOf(two));
	    
	    driver.findElement(OTPThree).click();
	    driver.findElement(OTPThree).sendKeys(String.valueOf(three));
	    
	    driver.findElement(OTPFour).click();
	    driver.findElement(OTPFour).sendKeys(String.valueOf(four));
	    
	    driver.findElement(OTPFive).click();
	    driver.findElement(OTPFive).sendKeys(String.valueOf(five));
	    
	    driver.findElement(OTPSix).click();
	    driver.findElement(OTPSix).sendKeys(String.valueOf(six));
	}
	public void enterActivatePassword(String password) {
		driver.findElement(ActivatePassword).clear();
		driver.findElement(ActivatePassword).sendKeys(password);
	}
	
	public String getActivatePassword() {
		String password =driver.findElement(ActivatePassword).getText();
		return password;
	}
	public void enterActivateConfirmPassword(String cpassword) {
		driver.findElement(ActivateConfirmPassword).clear();
		driver.findElement(ActivateConfirmPassword).sendKeys(cpassword);
	}
	
	public String getActivateConfirmPassword() {
		String cpassword =driver.findElement(ActivateConfirmPassword).getText();
		return cpassword;
	}
	public void activateAndLogin() {
		driver.findElement(ActivateAndLogin).click();
		
	}
	public boolean isPageDisplayed() {
	    return driver.findElement(PageDisplayed).isDisplayed();
	}
	public void BackButtonEmp() {
		driver.findElement(BackBtn).click();
	}
	public void MoreTwice() {
        WebElement moreElement = waitForElementVisibility(MoreTabs, 20);
        moreElement.click();  // First click
	}
	
	//Employee Page Inactive Employee
	public void clickInactiveButton() {
		driver.findElement(btnInactive).click();
	}
	public void clickActiveButton() {
		driver.findElement(btnActive).click();
	}
	public void clickUser() {
		driver.findElement(userClick).click();
	}
	public void clickDetailsButton() {
		driver.findElement(btnDetails).click();
	}
	public void clickActivateButton() {
		driver.findElement(btnActivate).click();
	}
	public void clickActivateButtonAgain() {
		driver.findElement(btnActivateAgain).click();
	}
	public boolean confirmVisibility() {
	    // Wait for the element to be visible
		WebElement moreElement = waitForElementVisibility(visibleServer, 10);
		return moreElement.isDisplayed();
	}
	
	//Activate an Employee
	public void clickActivateInactiveButton() {
		driver.findElement(btnDeactivate).click();
	}
	
	public void selectReason() {
		driver.findElement(reasonSelect).click();
		WebElement serverElemen = scrollTo("Poor Performance","android.widget.ScrollView");
        serverElemen.click();
		driver.findElement(btnDeactivateAgain).click();
	}
	
	
}
