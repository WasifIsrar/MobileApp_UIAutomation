package pages;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;

import basePage.BasePage;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import utils.LogUtils;

public class KDS extends BasePage{
	
    private By confirmBtn=By.id("tvConfirm");
    private By selectAllBtn=By.id("selectBtn");
    private By stationsView=By.id("rvStations");
    private By fulfillBtn=By.id("fulfillBtn");
    private By recyclerView=By.id("rvTickets");
    private By ticketPopup=By.id("clTicketpopup");
    private By ticketTime=By.id("tvTime");
    private By recentBtn=By.id("tvRecentFulfillOrders");
    private By itemInTicket=By.id("itemCL");
    private By modifier=By.id("ingredientnameTV");
    private By botStation=By.xpath("//android.widget.TextView[@text='Bot']");
    private By allItemsStation=By.xpath("//android.widget.TextView[@text='All Items']");
    private By ticketDetails=By.id("tvTicketDetails");
    private By fullScreenDialog=By.id("android:id/ok");
    private By station=By.id("tvStationName");
    private By itemName=By.id("itemnameTV");
    private By callServer = By.id("callServerBtn");
    private By ticketNumber = By.id("tvTicketNumber");
    private By timer = By.id("tv_cool_down_timer");
    private By specialRequestText = By.id("ingredientnameTV");
    private By orderNumber = By.id("TVorderno");

    
    String ticketId;
    int ticketCount;
    WebElement myTicket;


	public KDS(AndroidDriver driver) {
		super(driver);
	}
	
	public void setTicketId(String ticketId) {
		this.ticketId=ticketId;
	}
	
	public void checkBotStation() {
		WebElement view=driver.findElement(stationsView);
		((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", ImmutableMap.of(
				"elementId",((RemoteWebElement)view).getId(),
				"direction", "right",
				"percent", 10.0
		));
		driver.findElement(botStation).click(); 
	}
	
	public void selectFirstStation() {
		driver.findElement(station).click();
	}
	
	public void confirmStation() {
		driver.findElement(confirmBtn).click();
		
		getLogger().info("Waiting For KDS Ticket Screen");
		try {
			waitForElementPresent(recyclerView,5);
		}
		catch(Exception e) {
		}
	}
	public String getTicketNumber() {
		scroll();
		return driver.findElement(By.xpath("//android.widget.TextView[@text='#"+ticketId+"']")).getText().substring(1);
	}
	public boolean isTicketIdMatchingInKDS(String ticketId) {
	    List<WebElement> matchingTickets = driver.findElements(By.xpath("//android.widget.TextView[@text='#" + ticketId + "']"));
	    return !matchingTickets.isEmpty(); // Returns true if there is at least one matching ticket
	}

	
	public int getTwoTickets() {
		List<WebElement> ticket = driver.findElements(By.xpath("//android.widget.TextView[@text='#"+ticketId+"']"));
		return ticket.size();
	}
	
	public char getQuantity() {
		getLogger().info("Verifying Quantity on KDS");
		String ticketDetails=driver.findElement(By.xpath("//android.widget.TextView[@text='#"+ticketId+"']/parent::android.view.ViewGroup/following-sibling::android.widget.TextView[contains(@resource-id,'tvTicketDetails')]")).getText();
		System.out.println("TICKET DETAILS: "+ticketDetails);
		System.out.println("FIRST CHAR: "+ticketDetails.strip().charAt(0));
		return ticketDetails.strip().charAt(0);
	}
	
	public boolean verifyTimerStopped() {
		getLogger().info("Verifying Timer");
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(5));
		return wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
	        	return driver.findElement(ticketTime).getText().equals("0:00");
	        }
		});
		
	}

	public String getModifier(){
		getLogger().info("Getting modifier from KDS");
		String modifierText= driver.findElement(modifier).getText().trim();
		getLogger().info("ACTUAL MODIFIER: "+modifierText);
		return modifierText;
	}
	
	public void openTicket() {
		getLogger().info("Opening Ticket");
		driver.findElement(By.xpath("//android.widget.TextView[@text='#"+ticketId+"']")).click();
		
		getLogger().info("Waiting For Popup");
		waitForElementPresent(ticketPopup,5);
	}
	
	public void selectAll() {
		getLogger().info("Selecting All");
		driver.findElement(selectAllBtn).click();
	}
	
	public void selectItem(String name) {
		driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'"+name+"')]")).click();
	}
	
	public String getClickable() {
		return driver.findElement(fulfillBtn).getAttribute("clickable");
	}
	
	public void selectItem() {
		getLogger().info("Selecting Item");
		driver.findElements(itemInTicket).get(0).click();
	}
	
	public void fulfill() {
		getLogger().info("Fulfilling");
		driver.findElement(fulfillBtn).click();
		
	}
	
	public boolean isDisappear() {
		getLogger().info("Waiting For Ticket to disappear");
		return waitForElementInvisibility(By.xpath("//android.widget.TextView[@text='#"+ticketId+"']"),5);
	}
	
	public boolean isRecalled() {
		return waitForElementVisibility(By.xpath("//android.widget.TextView[@text='#"+ticketId+"']/parent::android.view.ViewGroup/following-sibling::android.widget.TextView[contains(@resource-id,'tvRecalled')]"),5)
		.isDisplayed();
	}
	
	public boolean verifyFulfilled(String item) {
		return driver.findElement(By.xpath("//android.widget.TextView[@text='1 "+item+"']/preceding-sibling::android.widget.ImageView[contains(@resource-id,'tickIV')]")).isDisplayed();
	}
	
	public boolean isTicketTime(){
		getLogger().info("Waiting For Fire Time to start again");
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(5));
		return wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
				WebElement ticketTime=driver.findElement(By.xpath("//android.widget.TextView[@text='#"+ticketId+"']/following-sibling::android.widget.TextView[contains(@resource-id,'tvTime')]"));
				System.out.println("TICKET TIME: "+ticketTime.getText());
				return isTimeGreaterThanZero(ticketTime.getText());
	        }
		});
	}
	
	/*
	 * Waits for fire time to start again
	 */
	public boolean isFireTime(){
		getLogger().info("Waiting For Fire Time to start again");
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(5));
		return wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
				WebElement fireTime=driver.findElement(By.xpath("//android.widget.TextView[@text='#"+ticketId+"']/parent::android.view.ViewGroup/following-sibling::android.widget.TextView[contains(@resource-id,'tvFireTimer')]"));
				System.out.println("FIRE TIME: "+fireTime.getText());
				return isTimeGreaterThanZero(fireTime.getText());
	        }
		});
	}
	
	public boolean doubleTap() {
		getLogger().info("Double Tapping to Fulfill");
		((JavascriptExecutor) driver).executeScript("mobile: doubleClickGesture", ImmutableMap.of(
			    "elementId", ((RemoteWebElement) driver.findElement(By.xpath("//android.widget.TextView[@text='#"+ticketId+"']"))).getId()
			));
		
		return waitForElementInvisibility(By.xpath("//android.widget.TextView[@text='#"+ticketId+"']"),5);
	}
	
	public String getTicketDetails() {
		List<WebElement> list_details=driver.findElements(ticketDetails);
		return list_details.get(list_details.size()-1).getText();
	}
	
	/*
	 * Scrolls Until Ticket displayed
	 * Performs max 10 scrolls
	 */
	private void scroll() {
		getLogger().info("Scrolling to Find the Ticket");
			List<WebElement> view=driver.findElements(recyclerView);
			if(view.size()>0) {
			int count=0;
			int maxCount=10;
			boolean atEnd=false;
			while(!atEnd&&count<maxCount) {
				((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", ImmutableMap.of(
						"elementId",((RemoteWebElement)view.get(0)).getId(),
						"direction", "right",
						"percent", 10.0
				));
				count++;
				atEnd=isAtEnd();
			}
		}
	}
	private boolean isAtEnd() {
		return driver.findElements(By.xpath("//android.widget.TextView[@text='#"+ticketId+"']")).size()==1;
	}
	
	public void tapRecent() {
		getLogger().info("Tapping Recently Fulfilled");
		driver.findElement(recentBtn).click();
		
		waitForElementPresent(By.xpath("//android.widget.TextView[@text='#"+ticketId+"']"),5);
	}
	
	public int getItemsCount() {
		List<WebElement> list1_items=driver.findElements(itemInTicket);
		return list1_items.size();
	}
	
	public String getServerName() {
		List<WebElement> serverList=driver.findElements(serverName);
		return serverList.get(serverList.size()-1).getText();
	}
	
	public void selectAllItemsStation() {
		driver.findElement(allItemsStation).click();
	}
	
	public void closeDialog() {
		try {
			driver.findElement(fullScreenDialog).click();
		}
		catch(Exception e) {
			getLogger().info("Full Screen Dialog did not appear");
		}
	}
	
	public String getTableNumber() {
		return driver.findElement(By.xpath("//android.widget.TextView[@text='#"+ticketId+"']/following-sibling::android.widget.TextView[contains(@resource-id,'tvTableNumber')]")).getText();
	}
	
	public void tapCallServer()
	{
		getLogger().info("Tapping Call Server");
		driver.findElement(callServer).click();
	}
	public boolean isCallServerButtonDisabled() {
        WebElement callServerButton = driver.findElement(callServer); 
        return !callServerButton.isEnabled(); 
    }
	
	public String getOrderNumber() {
		return driver.findElement(orderNumber).getText().substring(1);
	}
	
	public String getSpecialRequest() {
		return driver.findElement(specialRequestText).getText();
	}
	
	private boolean isTimeGreaterThanZero(String timeString) {
        try {
        	if (timeString.matches("\\d:\\d{2}")) {
                timeString = "0" + timeString; // Add a leading zero for single-digit hour
            }
            // Define the formatter for HH:mm format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            // Parse the input string to a LocalTime object
            LocalTime inputTime = LocalTime.parse(timeString, formatter);
            System.out.println("Input TIME: "+inputTime);

            // Define the time "00:00"
            LocalTime zeroTime = LocalTime.MIDNIGHT;
            System.out.println("IS Greater than zero: "+inputTime.isAfter(zeroTime));
            // Compare if the input time is after "00:00"
            return inputTime.isAfter(zeroTime);
        } catch (DateTimeParseException e) {
            // Handle the case where the input string is not in a valid format
        	getLogger().info("Invalid time format: " + e.getMessage());
            return false;
        }
    }
//	public String getTimerText() {
//	    return driver.findElement(timer).getText();
//	}
//
//	public boolean isTimerStarted() {
//	    String initialTime = getTimerText();
//	    System.out.println("Initial Timer: " + initialTime);
//	    try {
//	        Thread.sleep(1000); // Wait 1 second
//	    } catch (InterruptedException e) {
//	        e.printStackTrace();
//	    }
//	    String newTime = getTimerText();
//	    System.out.println("New Timer: " + newTime);
//	    return !initialTime.equals(newTime);
//	}

	


}