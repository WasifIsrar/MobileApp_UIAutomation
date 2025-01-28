package pages;

import com.aventstack.extentreports.ExtentTest;

import basePage.OnlineBasePage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class OrderingQRPage extends OnlineBasePage {
    public WebDriver driver;

    public By firstName = By.id("firstName");
    public By lastName = By.id("lastName");
    public By phoneNumber = By.id("phoneNumber");
    public By emailField = By.id("email");
    public By checkBox=By.cssSelector(".mui-p3293f");
    public By loginLink=By.xpath("//span[.='Login']");
        public By startOrderBtn=By.xpath("//div[@class='MuiBox-root mui-15x8as1']/button");
    public By qrChecked=By.cssSelector(".Mui-checked");
    public By startBtnEnabled=By.cssSelector(".mui-l3sbev");
    public By loginPageAssert=By.xpath("//p[.='Log In']");
    public By incorrectNameAssert=By.xpath("//h6[.='Incorrect name format']");
    public By mainMenuAssert=By.xpath("//p[.='Roboonline']");



    public void enterFirstName(String FirstName) {
        WebElement element = findTheElement(firstName);
        clearTextField(element);
        findTheElement(firstName).sendKeys(FirstName);

    }
    public void enterLastName(String LastName) {
        WebElement element = findTheElement(lastName);
        clearTextField(element);
        findTheElement(lastName).sendKeys(LastName);
    }
    public void enterPhoneNumber(String PhoneNumber) {
        WebElement phone= findTheElement(phoneNumber);
        phone.click();
        phone.sendKeys(PhoneNumber);
    }
    public void enterEmail(String Email) {
        findTheElement(emailField).sendKeys(Email);
    }
    public void clickCheckBox(ExtentTest methodNode) {
        step(methodNode, "Click on Check Box");
        pageLoadWait();
        WebElement checkBoxButton = findTheElement(checkBox);
        checkBoxButton.click();
    }
    public void clickStartOrderingBtn() throws InterruptedException {
        pageLoadWait();
//        closeKeyboard(2);
        WebElement startOrderingButton = findTheElement(startOrderBtn);
//        startOrderingButton.click();
        js.executeScript("arguments[0].click();", startOrderingButton);
    }
    public void clickLoginLink() {
        pageLoadWait();
        WebElement LoginLink = findTheElement(loginLink);
//        LoginLink.click();
        js.executeScript("arguments[0].click()", LoginLink);
    }
    public boolean checkedBoxAssert(){
        WebElement checkedBoxState = findTheElement(qrChecked);

        if (checkedBoxState != null) {
            System.out.println( "Checkbox is Checked");
            return true;
        } else {
            System.out.println( "Checkbox is not Checked");
            return false;
        }
    }
    public boolean startBtnAssert(){
        WebElement checkedBoxState = findTheElement(startBtnEnabled);

        if (checkedBoxState != null) {
            System.out.println( "Start Ordering Button is Enabled");
            return true;
        } else {
            System.out.println( "Start Ordering Button is not Enabled");
            return false;
        }
    }
    public boolean loginLinkAssert(){
        WebElement checkedBoxState = findTheElement(loginPageAssert);

        if (checkedBoxState != null) {
            System.out.println( "Login Page is Found");
            return true;
        } else {
            System.out.println( "Login Page is not Found");
            return false;
        }
    }
    public String getText(By Locator) {

        WebElement element=findTheElement(Locator);

        return element.getText();

    }
    
    public String firstNameAssert(){
        return getText(incorrectNameAssert);
    }
    
    public String lastNameAssert(){
        return getText(incorrectNameAssert);
    }
    
    public String mainMenuAssert(){
        return getText(mainMenuAssert);
    }


}
