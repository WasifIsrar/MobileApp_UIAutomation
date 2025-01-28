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

public class MomLostAndFoundPage extends BasePage {

	public MomLostAndFoundPage(AndroidDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	By addBtn = AppiumBy.accessibilityId("addMoreBtn");
	By itemLostbtn =AppiumBy.accessibilityId("Lost Item");
	By ItemName = AppiumBy.accessibilityId("textInput-Item name");
	By categorySelect = AppiumBy.accessibilityId("Select item category");
	By customerName = AppiumBy.accessibilityId("textInput-Customer name");
	By emailget = AppiumBy.accessibilityId("textInput-Email address");
	By phoneNumber = AppiumBy.accessibilityId("maskInputPhone number");
	By colorPrimary = AppiumBy.accessibilityId("textInput-Primary color");
	By colorOther = AppiumBy.accessibilityId("textInput-Other color");
	By areaService = AppiumBy.accessibilityId("textInput-Service area");
	By enterDescription = AppiumBy.accessibilityId("Description Box");
	By btnCreate = AppiumBy.accessibilityId("Create");
	By dateConfirm = By.xpath("(//android.widget.TextView[@text=\'Date Reported:\'])[1]");
	By itemConfirm = By.xpath("//android.widget.TextView[@text=\'Date Reported:\']/preceding-sibling::android.widget.TextView");
	By emailField = By.xpath("//android.widget.EditText[@text='Enter Your Email Address']");
	By itemFoundbtn =AppiumBy.accessibilityId("Found Item");

	
	
	public void plusButton() {
		driver.findElement(addBtn).click();
	}
	public void lostItem() {
		driver.findElement(itemLostbtn).click();
	}
	public void foundItem() {
		driver.findElement(itemFoundbtn).click();
	}
	public String randomName() {
		 return "Item" + UUID.randomUUID().toString().substring(0, 6);
	}
	public void enterItemName() {
		String random = randomName();
		driver.findElement(ItemName).clear();
		driver.findElement(ItemName).sendKeys(random);
		
	}
	
	public String getItemName() {
		String firstName = driver.findElement(ItemName).getText();
		return firstName;
		
	}
	
	public void selectCategory() {
		driver.findElement(categorySelect).click();
		WebElement serverElemen = scrollTo("Clothing","android.widget.ScrollView");
        serverElemen.click();
	}
	
	public void enterCustomerName(String cName) {
		driver.findElement(customerName).clear();
		driver.findElement(customerName).sendKeys(cName);
	}
	public String getCustomerName() {
		String customerNa = driver.findElement(customerName).getText();
		return customerNa;
	}
	public String enteringEmail() {
        String eEmail = generateRandomEmail();
        driver.findElement(emailget).clear();
        driver.findElement(emailget).sendKeys(eEmail);
        return eEmail;
    }

    public String gettingEmail() {
        return driver.findElement(emailget).getText();
    }
	public String generateRandomPhoneNumber() {
	    String countryCode = "+92";  // Fixed country code
	    String areaCode = "333";     // Fixed area code for your example
	    Random random = new Random();
	    int firstPart = random.nextInt(900) + 100;  // Generates a 3-digit number between 100 and 999
	    int secondPart = random.nextInt(9000) + 1000;  // Generates a 4-digit number between 1000 and 9999
	    return String.format("%s (%s) %03d-%04d", countryCode, areaCode, firstPart, secondPart);
	}
	
	public String enterPhoneNumber() {
		String phone = generateRandomPhoneNumber();
		driver.findElement(phoneNumber).clear();
		driver.findElement(phoneNumber).sendKeys(phone);
        return phone;
	}
	public String getPhoneNumber() {
        return driver.findElement(phoneNumber).getText();
    }
	public void enterPrimaryColour(String pName) {
		driver.findElement(colorPrimary).clear();
		driver.findElement(colorPrimary).sendKeys(pName);
	}
	public String getPrimaryColour(){
		String ColourNameP = driver.findElement(colorPrimary).getText();
		return ColourNameP;
	}
	
	public void enterOtherColour(String oName) {
		driver.findElement(colorOther).clear();
		driver.findElement(colorOther).sendKeys(oName);
	}
	public String getOtherColour() {
		String ColourNameO = driver.findElement(colorOther).getText();
		return ColourNameO;
	}
	public void scrollToService() {
		WebElement serverElemen = scrollTo("Create","android.widget.ScrollView");
	}
	public void enterServiceArea(String service) {
		driver.findElement(areaService).clear();
		driver.findElement(areaService).sendKeys(service);
	}
	
	public String getServiceArea() {
		String ColourName = driver.findElement(areaService).getText();
		return ColourName;
	}
	public String randomDescription() {
	    String[] subjects = {"The product", "This item", "The material", "This design" };
	    String[] verbs = {"is lost at the"};
	    String[] objects = {"Kitchen.", "Dining Area.", "table number 45", "first floor."};
	    Random rand = new Random();
	    String description = subjects[rand.nextInt(subjects.length)] + " " + 
	                         verbs[rand.nextInt(verbs.length)] + " " + 
	                         objects[rand.nextInt(objects.length)];
	    return description;  
	}

	public void enterDescription() {
		String description = randomDescription();
	    driver.findElement(enterDescription).clear();
	    driver.findElement(enterDescription).sendKeys(description);
	}
	public void clickCreateButton() {
	    driver.findElement(btnCreate).click();
	}
	public String getLostItem() {
		return driver.findElement(itemConfirm).getText();
		
	}
//	 public String generateRandomEmail() {
//	        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//	        LocalDateTime now = LocalDateTime.now();
//	        String baseEmail = "testuser";
//	        String domain = "@example.com";
//	        return baseEmail + dtf.format(now) + domain;
//	    }
//
	
//	    // Method to enter the email in the email field
//	    public String enterEmail() {
//	    	String eEmail = generateRandomEmail();  // Generate a new email
//	        driver.findElement(emailField).clear();
//	        driver.findElement(emailField).sendKeys(eEmail);
//	        return eEmail;
//	    }
//
//	    // Method to get the entered email from the email field
//	    public String getEmail() {
//	        return driver.findElement(emailField).getText();
//	    }


}
