package basePage;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OnlineBasePage {

    public By restaurantLocation = By.xpath("//input[@class='inputtype pac-target-input']");
    public By locationDropDown = By.xpath("//div[@class='pac-item']");
    public By nextBtn = By.xpath("//button[@type='button']");
    public By confirm = By.xpath("//button[.='Confirm Pick Up']");
    public By loginBtn = By.xpath("//div[.='Log In']");

    // OnlineQR BasePage
    public By loginLink=By.xpath("//span[.='Login']");
    public By numberField = By.id("username");
    public By nextButton=By.xpath("//button[.='Next']");
    public By passwordField = By.id("password");
    public By loginButton=By.xpath("//button[.='Log In']");
    public By mainMenuText=By.xpath("//p[.='AIO CAFE']");
    public By ourRestauratnt = By.xpath("//span[.='Roboonline']");
    public By myRestaurant = By.xpath("//label[contains(.,'RoboOnline')]/span[1]");

//    public static WebDriver driver;
    public static WebDriver driver;
    public static WebDriverWait wait;
    public static JavascriptExecutor js;
    public static Random random;

    // Default constructor
    public OnlineBasePage() {
    }

    public static void setDriver(WebDriver webDriver) {
        driver = webDriver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    onlineLoginCall login = new onlineLoginCall();

    public void loginCall(ExtentTest methodNode, String username, String password, String endpoint) throws Exception {
        step(methodNode, "Login user with valid credentials");
        login.loginRequest(username, password, endpoint);
        Cookie accessTokenCookie = new Cookie("AccessToken", onlineLoginCall.accessToken);
        Cookie idTokenCookie = new Cookie("IdToken", onlineLoginCall.idToken);
        Cookie refreshTokenCookie = new Cookie("RefreshToken", onlineLoginCall.refreshToken);
        Cookie subCookie = new Cookie("sub", onlineLoginCall.sub);

        driver.manage().addCookie(accessTokenCookie);
        driver.manage().addCookie(idTokenCookie);
        driver.manage().addCookie(refreshTokenCookie);
        driver.manage().addCookie(subCookie);

        js.executeScript("window.sessionStorage.setItem('AccessToken', arguments[0]);", onlineLoginCall.accessToken);
        js.executeScript("window.sessionStorage.setItem('IdToken', arguments[0]);", onlineLoginCall.idToken);
        js.executeScript("window.sessionStorage.setItem('RefreshToken', arguments[0]);", onlineLoginCall.refreshToken);
        js.executeScript("window.sessionStorage.setItem('sub', arguments[0]);", onlineLoginCall.sub);

        js.executeScript("window.localStorage.setItem('phone', arguments[0]);", onlineLoginCall.numWithCode);
//        js.executeScript("window.localStorage.setItem('fName', arguments[0]);", "Humayun");
//        js.executeScript("window.localStorage.setItem('lName', arguments[0]);", "Ali");

        driver.navigate().refresh();
        pageLoadWait();
        waitForElementToBeVisible(ourRestauratnt);
    }

    public void pickRestaurant(ExtentTest methodNode, String locationText, String expectedRestaurantName) throws InterruptedException {
        step(methodNode, "Select nearby restaurant by location");

        WebElement locationInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        restaurantLocation)
        );

        js.executeScript("arguments[0].value='" + locationText + "';", locationInput);
        js.executeScript("arguments[0].dispatchEvent(new Event('input', {bubbles: true}));", locationInput);
        js.executeScript("arguments[0].dispatchEvent(new Event('change', {bubbles: true}));", locationInput);

        WebElement locationDropDownElement = wait.until(
                ExpectedConditions.elementToBeClickable(
                        locationDropDown)
        );

        locationDropDownElement.click();
        WebElement nextButton = driver.findElement(nextBtn);

        try {

            WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            driverWait.until(driver -> false);

        } catch (Exception e) {

            // Intentionally left empty
            Thread.sleep(5000);

        }

        try {

            js.executeScript("arguments[0].scrollIntoView(true);", nextButton);
            js.executeScript("arguments[0].click();", nextButton);
            findTheElement(myRestaurant).click();
            findTheElement(confirm).click();
            waitForElementToBeVisible(loginBtn);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(loginBtn));
            WebElement modal = findTheElement(loginBtn);
            boolean isButtonDisplayed = modal.isDisplayed();

            if (isButtonDisplayed) {
                System.out.println("The button was appeared.");
            } else {
                System.out.println("No Button");
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

//    public void loginOnline(ExtentTest methodNode, String Number,String Password){
//        step(methodNode,"Login online");
//        findTheElement(loginButton).click();
//        findTheElement(numberField).sendKeys(Number);
//        findTheElement(nextButton).click();
//        findTheElement(passwordField).sendKeys(Password);
//        findTheElement(loginButton).click();
//        waitForElementToBeVisible(mainMenuText);
//
//    }
//
//    public void loginQR(ExtentTest methodNode,String Number,String Password){
//        step(methodNode,"Login onlineQR");
//        findTheElement(loginLink).click();
//        findTheElement(numberField).sendKeys(Number);
//        findTheElement(nextButton).click();
//        findTheElement(passwordField).sendKeys(Password);
//        findTheElement(loginButton).click();
//        waitForElementToBeVisible(mainMenuText);
//
//    }

    public static void closeKeyboard(int seconds) {
        if (driver instanceof AndroidDriver) {
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
        }
    }


    public static String generateNumber() {
         random = new Random();
        int firstDigit = random.nextInt(9) + 1;
        StringBuilder phoneNumber = new StringBuilder();
        phoneNumber.append(firstDigit);
        for(int i = 0; i < 9; i++) {
            phoneNumber.append(random.nextInt(10));
        }

        return phoneNumber.toString();
    }

    /** Dynamic logger for BasePage and its subclasses
     *  Log to console
     *  Log to ExtentReports
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void testSteps(String stepDescription) {
        logger.info("Step: " + stepDescription);
    }

    public void step(ExtentTest methodNode, String val) {
        testSteps(val);

        if (methodNode != null) {
            methodNode.log(Status.INFO, val);
        } else {
            System.err.println("Warning: MethodNode is null, unable to log to ExtentReports");
        }

    }


    private FluentWait<WebDriver> configureFluentWait() {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery((Duration.ofMillis(200)))
                .ignoring((NoSuchElementException.class));
    }

    public WebElement fluentwaitUntilClickable(By locator) {
        return configureFluentWait()
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement fluentwaitUntilVisible(By locator) {
        return configureFluentWait()
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementToBeVisible(By locator) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        return explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementToBeClickable(By locator) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        return explicitWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForElementPresence(By locator) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        return explicitWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public void pageLoadWait() {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
    }

    public WebElement findTheElement(By locator) {
        try {
            return waitForElementToBeVisible(locator);
        } catch (NoSuchElementException e) {
            System.out.println("Element not found: " + locator.toString());
            return null;
        }
    }

    public void clearTextField(WebElement element) {

        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);
    }

    public List<WebElement> findElements(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            System.out.println("Elements not found within timeout: " + locator.toString());
            return Collections.emptyList();  // Return an empty list if no elements are found
        }
    }

    public void waitUntilElementIsClickable(By locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    public int getRandomValue(int size){
        if (size <= 0) {
            return -1;
        }
        random = new Random();
        return random.nextInt(size);
    }


}