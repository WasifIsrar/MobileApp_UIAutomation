package basePage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import pages.DashboardPage;
import pages.LoginPage;
//import pages.MomShiftSchedularPage;
import pages.MorePage;
import pages.OrderPage;
import pages.OrdershubPage;
import utils.LogUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BasePage {
    public AndroidDriver driver;
    public By continueBtn = By.id("btnContinue");
    public By tableLayout = By.id("rvTableLayouts");
    public By splitTicketBtn = By.id("btnSplitTicket");
    public By doneBtn = By.id("btnDone");
    public By noteLabel = By.id("tvNote");
    public By numpadDoneBtn = By.id("btn_done");
    public By backBtn = By.id("ivBack");
    public By backMOMBtn = AppiumBy.accessibilityId("Go Back");
    public By btn1 = By.id("btn_1");
    public By btn2 = By.id("btn_2");
    public By btn0 = By.id("btn_0");
    public By noReceiptBtn = By.id("noReceiptBtn");
    public By profileIcon = By.id("ivDummy");
    public By cashDrawerBtn = By.id("tvCashDrawer");
    public By cashLogs = By.xpath("//android.widget.TextView[@text='Cash Logs']");
    public By hamburgerMenu = By.id("ivHamburgerMenu");
    public By ordersHubBtn = By.id("clOrderHub");
    public By tableView = By.id("frameLayout");
    public By tables = By.id("clMain");
    public By dashboardBtn = By.id("tvDashBoard");
    public By modifierPrice = By.id("tvModifierPrice");
    public By tableServeOption = By.xpath("//android.widget.TextView[@text='Table Serve']");
    public By confirmationMessage = By.id("tvMessage");
    public By cancelBtn = By.id("btnCancel");
    public By serverName = By.id("tvServerName");
    private static final Logger baseLogger = LoggerFactory.getLogger(BasePage.class);
    public By modifier = By.id("tvModifier");
    public By emailField = AppiumBy.accessibilityId("textInput-Email address");
    public By nextButton = AppiumBy.accessibilityId("Next");
    public By passwordField = AppiumBy.accessibilityId("textInput-Password");
    public By profileTab = AppiumBy.accessibilityId("Profile");
    public By docTab = AppiumBy.accessibilityId("Documents");
    public By pageTitle = AppiumBy.accessibilityId("title");
    public By submitBtn = AppiumBy.accessibilityId("Submit");
    public By saveBtn = AppiumBy.accessibilityId("Save");
    public By titleOfMom = AppiumBy.accessibilityId("appName");
    public By loginButton = AppiumBy.accessibilityId("Log in");
    public By loginButtonAgain = AppiumBy.accessibilityId("Log In");
    protected By checkoutBtn = By.id("vCheckout");
    public By kioskSubtotal = By.id("tvAmount");
    private By completePaymentBtn = By.id("completePaymentBtn");
    public By payBtn = By.id("btnPay");
    public By sendAndProceedBtn = By.id("sendProceedBtn");
    public By stayBtn = By.id("btnStay");
    public By activityStatus = By.id("tvActivityStatus");
    public By sendBtn = By.id("btnSend");
    public By splitTicketScreen = By.id("TvSplitTicket");
    public By readyBtn = By.id("tvReady");
    public By getCallServerText = By.xpath("//android.widget.TextView[contains(@text, 'Chef')]");
    public By MoreBtn = AppiumBy.accessibilityId("More");
    public By emailEmployee = AppiumBy.accessibilityId("textInput-Email Address");
    public By autoGratuity = By.xpath("//android.widget.CheckBox[contains(@text,'Auto Gratuity')]");
    public By shiftSchedular = By.xpath("//android.widget.TextView[@text='Shifts']");
    public By addBtn = AppiumBy.accessibilityId("addMoreBtn");
    public By QSRbtn = By.xpath("//android.widget.TextView[@text='Quick Serve']");
    public By phoneNumber = By.id("etPhoneNumber");
    private By pinScreen = By.id("tvEnter");
    protected By mposSplash = By.id("clSplash");
    protected By noBtn = By.id("btnNo");
	public By exactAmount=By.xpath("//android.widget.Button[contains(@resource-id,'suggestionText')]");


    public BasePage(AndroidDriver driver) {
        this.driver = driver;
    }

    protected Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    public void isMomPageDisplayed() {
        waitForElementPresent(titleOfMom, 60);
    }

    public LoginPage gotoLogin() {
        driver.findElement(loginButton).click();
        return new LoginPage(driver);
    }

    public WebElement waitForElementPresent(By element, int time) {
        return new WebDriverWait(driver, Duration.ofSeconds(time)).until(ExpectedConditions.presenceOfElementLocated(element));
    }
    
    public WebElement waitForElementToBeClickable(By element, int time) {
    	return new WebDriverWait(driver, Duration.ofSeconds(time)).until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForElementVisibility(By element, int time) {
        return new WebDriverWait(driver, Duration.ofSeconds(time)).until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    public List<WebElement> waitForElementsPresent(By element, int time) {
        return new WebDriverWait(driver, Duration.ofSeconds(time)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
    }

    public boolean waitForElementInvisibility(By element, int time) {
        return new WebDriverWait(driver, Duration.ofSeconds(time)).until(ExpectedConditions.invisibilityOfElementLocated(element));
    }

    public boolean waitForElementInvisibility(WebElement element, int time) {
        return new WebDriverWait(driver, Duration.ofSeconds(time)).until(ExpectedConditions.invisibilityOf(element));
    }

    public List<WebElement> waitForElementsVisibility(By element, int time) {
        return new WebDriverWait(driver, Duration.ofSeconds(time)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(element));
    }
    
    public void configureFluentWait(int timeInSeconds, int pollyingInMillis) {
        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeInSeconds))
                .pollingEvery((Duration.ofMillis(pollyingInMillis)))
                .ignoring((Exception.class));
    }

    public void tapByCoordinates(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tap));
    }

    public OrdershubPage gotoOrdershub() {
        baseLogger.info("Navigating to Ordershub");
        driver.findElement(hamburgerMenu).click();
        driver.findElement(ordersHubBtn).click();
        return new OrdershubPage(driver);
    }

    public String getDeviceTime() {
        String time = driver.getDeviceTime();
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(time);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return offsetDateTime.format(formatter);
    }

    public WebElement scrollTo(String onText,String className) {
        baseLogger.info("Scrolling to " + onText);
     return driver.findElement(AppiumBy.androidUIAutomator(
        		"new UiScrollable(new UiSelector().scrollable(true).className(\"" + className + "\"))" +
        		".scrollIntoView(new UiSelector().textMatches(\"(?i)" + onText + "\"));"
        		));
    }
    
    public WebElement scrollToById(String onText, String resourceId) {
        baseLogger.info("Scrolling to text: " + onText + " within resourceId: " + resourceId);
        return driver.findElement(AppiumBy.androidUIAutomator(
            "new UiScrollable(new UiSelector().scrollable(true).resourceId(\"" + resourceId + "\"))" +
            ".scrollIntoView(new UiSelector().textContains(\"" + onText + "\"));"
        ));
    }


    public void gotoTableServe() {
        baseLogger.info("Opening Table Serve");
        driver.findElement(hamburgerMenu).click();
        driver.findElement(tableServeOption).click();
    }

    public boolean isTableVisible() {
        baseLogger.info("Waiting For Tables Screen");
        return waitForElementVisibility(tables, 10).isDisplayed();
    }

    public void done() {
        getLogger().info("Clicking Split Done Button");
        driver.findElement(doneBtn).click();
    }

    public void completePayment() {
        getLogger().info("Completing Payment");
        driver.findElement(completePaymentBtn).click();
    }

    public void pay() {
        getLogger().info("Tapping Pay Button");
        driver.findElement(payBtn).click();
    }

    public void proceed() {
        driver.findElement(sendAndProceedBtn).click();
    }

    public boolean stay() {
        getLogger().info("Clicking Stay Button");
        boolean isClicked = false;
        int count=0;
        while (!isClicked&&count<5) {
            waitForElementVisibility(stayBtn, 5).click();
            getLogger().info("Waiting For Stay Status");
            if (driver.findElements(activityStatus).size() > 0) {
                isClicked = true;
            }
            count++;
        }
        return isClicked;
    }

    public String getModifierText() {
        return driver.findElement(modifier).getText();
    }

    public String getCallServerText() {
        return driver.findElement(getCallServerText).getText();
    }

    public MorePage clickMoreBtn() {
        waitForElementVisibility(MoreBtn, 10).click();
        return new MorePage(driver);
    }
    public void clickBackButton() {
		driver.findElement(backMOMBtn).click();
	}

    public String generateRandomEmail() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        String baseEmail = "testuser";
        String domain = "@example.com";
        return baseEmail + dtf.format(now) + domain;
    }

    public String enterEmail() {
        String eEmail = generateRandomEmail();
        driver.findElement(emailEmployee).clear();
        driver.findElement(emailEmployee).sendKeys(eEmail);
        return eEmail;
    }

    public String getEmail() {
        return driver.findElement(emailEmployee).getText();
    }

    public void uncheckAutoGratuity() {
        driver.findElement(autoGratuity).click();
    }

    public void clickCancelButton() {
        getLogger().info("Clicking Cancel Button");
        driver.findElement(cancelBtn).click();
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }

    public static String generateRandomNumberString(int length) {
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder randomNumberString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(numbers.length());
            randomNumberString.append(numbers.charAt(index));
        }
        return randomNumberString.toString();
    }
    
    public static String generateValidPhoneNumber() {
        Random random = new Random();
        StringBuilder phoneNumber = new StringBuilder();

        // First digit (area code) must be 2-9
        phoneNumber.append(random.nextInt(8) + 2); // Generates a number from 2 to 9

        // Next two digits for area code (no restrictions, so 0-9)
        phoneNumber.append(random.nextInt(10));
        phoneNumber.append(random.nextInt(10));

        // Fourth digit must be 2-9
        int fourthDigit = random.nextInt(8) + 2;
        phoneNumber.append(fourthDigit);

        // Fifth digit must not form '11' with the fourth
        int fifthDigit;
        do {
            fifthDigit = random.nextInt(10); // Generates a number from 0 to 9
        } while (fourthDigit == 1 && fifthDigit == 1); // Avoid "11" in fourth and fifth position

        phoneNumber.append(fifthDigit);

        // Remaining 5 digits can be any number from 0-9
        for (int i = 0; i < 5; i++) {
            phoneNumber.append(random.nextInt(10));
        }

        return phoneNumber.toString();
    }

//    public MomShiftSchedularPage clickShiftSchedular() {
//        waitForElementVisibility(shiftSchedular, 10).click();
//        return new MomShiftSchedularPage(driver);
//    }

    public void plusButton() {
        driver.findElement(addBtn).click();
    }

    public OrderPage clickQSR() {
    	waitForElementVisibility(QSRbtn,10);
        driver.findElement(QSRbtn).click();
        return new OrderPage(driver);
    }

    public String getRandomString(int length) {
        String characters = "23456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }

    public boolean isPinScreenDisplayed() {
        return driver.findElement(pinScreen).isDisplayed();
    }
    
    public void enterManagerPin(char [] pin) {
    	getLogger().info("Entering Manager Pin");
    	
    	for(int i = 0; i < pin.length; i++)
    	{
    		By btn_xpath = By.xpath("//android.widget.TextView[@text='" + pin[i] + "']");
    		driver.findElement(btn_xpath).click();
    	}
    }
    public void enterPOSManagerPin(char [] pin) {
    	getLogger().info("Entering Manager Pin");
    		driver.findElement(By.xpath("//android.widget.TextView[@text='"+pin[0]+"']")).click();
    		driver.findElement(By.xpath("//android.widget.TextView[@text='"+pin[1]+"']")).click();
    		driver.findElement(By.xpath("//android.widget.TextView[@text='"+pin[2]+"']")).click();
    		driver.findElement(By.xpath("//android.widget.TextView[@text='"+pin[3]+"']")).click();
    		driver.findElement(By.xpath("//android.widget.TextView[@text='"+pin[4]+"']")).click();
    		driver.findElement(By.xpath("//android.widget.TextView[@text='"+pin[5]+"']")).click();
    	
    }
    public DashboardPage openDashboard() {
		getLogger().info("Opening Dashboard");
		driver.findElement(profileIcon).click();
		driver.findElement(dashboardBtn).click();
		return new DashboardPage(driver);
	}
  
}
