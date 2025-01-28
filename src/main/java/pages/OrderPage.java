package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import utils.LogUtils;

public class OrderPage extends BasePage {

	private By orderScreen = By.id("rvTicketItems");
	private By category = By.xpath("//android.widget.TextView[@text='Bot']");
	private By modifierGroup2 = By.xpath("(//android.widget.TextView[contains(@resource-id,'tvMenuItem')])[2]");
	private By aroundWorld = By.xpath("//android.widget.TextView[@text='Around The World']");
	private By slider = By.xpath("//android.widget.TextView[@text='Slider F']");
	private By taco = By.xpath("//android.widget.TextView[@text='Taco F']");
	private By french = By.xpath("//android.widget.TextView[@text='French Fry F']");
	private By saladAdd = By.xpath("//android.widget.TextView[@text='Sal Add ons']");
	private By level1 = By.xpath(
			"(//androidx.recyclerview.widget.RecyclerView[contains(@resource-id,'rvItems')])[1]/android.view.ViewGroup");
	private By level2 = By.xpath(
			"(//androidx.recyclerview.widget.RecyclerView[contains(@resource-id,'rvItems')])[2]/android.view.ViewGroup");
	private By specialRequest = By.xpath("//android.widget.TextView[@text='Special Request']");
	private By itemComps = By.xpath("//android.widget.TextView[@text='Item Comps']");
	private By selectPercent = By.xpath("//android.widget.TextView[@text='Open %']");
	private By selectDollar = By.xpath("//android.widget.TextView[@text='Open $']");
	private By numppad = By.id("clBottomNumPad");
	private By specialRequestField = By.id("etSpecialRequest");
	private By customItem = By.xpath("//android.widget.TextView[@text='Custom Item']");
	private By customItemPrice = By.id("etPrice");
	private By customAddBtn = By.id("btnAdd");
	private By requestDoneBtn = By.id("btnSRDone");
	private By addedItem = By
			.xpath("//androidx.recyclerview.widget.RecyclerView[contains(@resource-id,'rvTicketItems')]/child::*");
	private By serviceCharges = By.xpath(
			"//android.view.ViewGroup[contains(@resource-id,'clServiceCharges')]/android.widget.TextView[contains(@resource-id,'tvCharges')]");
	private By subTotalAmount = By.id("tvSubTotalAmount");
	private By sendAndProceedBtn = By.id("sendProceedBtn");
	private By ticketNumber = By.id("tvTicketNumber");
	private By tableNumber = By.id("tvTableNumber");
	private By payCashBtn = By.id("btnByCash");
	private By item = By.id("tvItem");
	private By removeBtn = By.id("btnRemove");
	private By threeDotsBtn = By.id("ivKebebMenu");
	private By voidOrderBtn = By.xpath("//android.widget.TextView[@text='Void Order']");
	private By trainingOption = By
			.xpath("//android.widget.TextView[@text='Training testing']/preceding-sibling::android.widget.RadioButton");
	private By voidBtn = By.id("btnVoid");
	private By stayBtn = By.id("btnStay");
	private By activityStatus = By.id("tvActivityStatus");
	private By continueBtn = By.id("btnContinue");
	private By totalAmount = By.id("tvTotalAmount");
	private By holdBtn = By.id("btnHold");
	private By heldStatus = By.id("ivStatus");
	private By addBtn = By.id("ivAdd");
//	private By addBtn = By.xpath("(//android.widget.TextView[@resource-id='tvMenuItem' ]");
	private By donePagBtn = By.id("btnDonePag");
	private By quantityLabel = By.id("tvQty");
	private By ticketsNum = By.id("dot");
	private By editBtn = By.id("ivEdit");
	private By noteField = By.id("etNote");
	private By discountBtn = By.xpath("//android.widget.TextView[@text='Discount']");
	private By openPercentCheckBtn = By.xpath("//android.widget.TextView[@text='Open % Check']");
	private By openDollarsCheckBtn = By.xpath("//android.widget.TextView[@text='Open $ Check']");
	private By managerDiscountCheckBtn = By.xpath("//android.widget.TextView[@text='Manager Comp - Check']");
	private By taxCheckbox = By.xpath("//android.widget.TextView[@text='Tax Exemption']");
	private By totalTaxAmount = By.id("tvTaxTotalAmount");
	private By balanceDuePayment = By.id("amountTxt");
	private By total = By.id("totalET");
	private By optionsList = By.className("android.widget.ListView");
	private By itemsAmount = By.id("tvTotal");
	private By modifierPrice = By.id("tvModifierPrice");
	private By incrementBtn = By.id("incrementBtn");
	private By paymentAmount = By.id("paymentAmount_ET");
	private By remainingAmount = By.id("remainingAmount");
	private By billStatus = By.id("billStatusTV");
	private By changeTableBtn = By.xpath("//android.widget.TextView[@text='Change Table']");
	private By mainMenuBtn = By.id("btnMainMenu");
	private By breadcrumb = By.id("tvBreadCrumb");
	private By guestCount = By.id("rvGuest");
	private By guestsBtn = By.id("btnGuests");
	private By numpadClearBtn = By.id("btn_clear");
	private By ticketPayment = By.id("billFragmentContainer");
	private By paymentBackBtn = By.id("backPressBtn");
	private By balanceDue = By.id("tvBalanceAmount");
	private By serviceChargesPayment = By.id("serviceChargesET");
	private By subTotalPayment = By.id("subTotalET");
	private By taxPayment = By.id("taxET");
	private By balanceDueLeft = By.id("balanceDueET");
	private By changeServerBtn = By.xpath("//android.widget.TextView[@text='Change Server']");
	private By changeBtn = By.id("btnChange");
	private By discardBtn = By.id("btnDiscard");
	private By infoIcon = By.id("ivInfo");
	private By itemDetailPopup = By.id("itemdetailpopup");
	private By closeIcon = By.id("closeIV");
	private By cardBtn = By.id("btnByCard");
	private By backspaceBtn = By.id("btn_backspace");
	private By serviceChargesList = By.id("rvCharges");
	private By guests = By.id("tvGuestNumber");
	private By price = By.id("tvPrice");
	private By discountAmount = By.id("tvDiscountAmount");
	private By menuView = By.id("menuViewHolder");
	private By decreaseQuantity = By.id("ivSubtract");
	private By updateModifiersBtn = By.id("btnUpdateModifiers");
	private By payTenderBtn = By.id("btnByTender");
	private By serverWithoutTip = By.xpath("//android.widget.TextView[@text='Server Without Tip']");
	private By clickCustomer = By.id("etCustomerName");
	private By newCustomer = AppiumBy.accessibilityId("New Customer");
	private By nameFirst = By.id("etFirstName");
	private By nameLast = By.id("etLastName");
	private By phoneNumber = By.id("etPhoneNumber");
	private By btnSave = By.id("btnSave");
	private By customerName = By.id("tvCustomerName");
	private By listCustomers = By.id("rvCustomers");
	private By listCustomerName = By.id("tvName");
	private By serverList = By.id("rvServersList");
	private By serverRow = By.id("tvServerName");
	private By takeoutBtn = By.id("ivTakeAway");
	private By manualSc =By.xpath("//android.widget.CheckBox[contains(@text,'Manual')]");
	private By btnDone =By.id("btnDone");
	private By searchField=By.id("etSearch");
	private By searchedItem =By.xpath("(//android.widget.TextView[contains(@resource-id,'tvMenuItem')])");
	private By managerApproval = By.xpath("//android.widget.TextView[@text='Please enter manager’s PIN']");
	private By noModifier = By.xpath("//android.widget.TextView[@text='No']");

	public OrderPage(AndroidDriver driver) {
		super(driver);
	}

	// Return true if Order Screen gets Displayed
	public boolean isPageDisplayed() {
		getLogger().info("Waiting For Order Screen to Display");
		return waitForElementPresent(orderScreen, 10).isDisplayed();
	}

	public void clickOrderScreen() {
		driver.findElement(orderScreen).click();
	}
	
	public String getTableNumber() {
		String tableNum = driver.findElement(tableNumber).getText();
		String[] parts = tableNum.split("\\s+");
		if (parts.length >= 2) {
			getLogger().info(parts[1]);
			return parts[1];
		}
		return "";
	}

	public String getServerName() {
		String tableNum = driver.findElement(tableNumber).getText();
		String[] parts = tableNum.split("\\|");
		return parts[1].trim();
	}

	public void addItem() {
		getLogger().info("Adding Item");
		waitForElementVisibility(aroundWorld, 5).click();

	}

	public void addSecondItem() {
		getLogger().info("Adding Second Item");
		waitForElementVisibility(slider, 5).click();
	}

	public void addThirdItem() {
		getLogger().info("Adding Third Item");
		waitForElementVisibility(taco, 5).click();
	}

	public void addFourthItem() {
		getLogger().info("Adding Third Item");
		waitForElementVisibility(french, 5).click();
	}

	public void selectItem() {
		getLogger().info("Selecting Category");
		driver.findElement(By.xpath("//android.widget.TextView[@text='Coffee']")).click();
		driver.findElements(level2).get(0).click();
	}

	public boolean verifyBreadcrumbsRemoved() {
		getLogger().info("Going back to main menu");
		driver.findElement(mainMenuBtn).click();

		return driver.findElements(breadcrumb).size() == 1;
	}

	/*
	 * Checks if item added to order
	 */
	public Boolean isItemAdded(int count) {
		getLogger().info("Checking if Item Added");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		return wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return driver.findElements(addedItem).size() >= count;
			}
		});
	}

	public void waitForSubtotal() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return Double.parseDouble(driver.findElement(subTotalAmount).getText().substring(1)) > 0;
			}
		});
	}

	public void addMultiple(int count) {
		getLogger().info("Increasing Quantity to 10");
		WebElement plusBtn = driver.findElement(addBtn);
		for (int i = 0; i < count; i++) {
			plusBtn.click();
		}
	}

	public void decreaseItemQuantity(int count) {
		getLogger().info("Decreasing Quantity");
		WebElement minusBtn = driver.findElement(decreaseQuantity);
		for (int i = 0; i < count; i++) {
			minusBtn.click();
		}
	}

	public List<Double> getItemPrices() {
		List<WebElement> list1_itemPrice = driver.findElements(itemsAmount);
		list1_itemPrice.remove(0);
		List<Double> list_prices = new ArrayList<Double>();
		for (WebElement itemPrice:list1_itemPrice) {
			System.out.println(itemPrice.getText().substring(1));
			list_prices.add(Double.parseDouble(itemPrice.getText().substring(1)));
		}
		return list_prices;
	}

	public List<Double> getModifierPrices() {
		List<WebElement> list1_itemPrice = driver.findElements(modifierPrice);
		List<Double> list_prices = new ArrayList<Double>();
		for (int i = 0; i < list1_itemPrice.size(); i++) {
			list_prices.add(Double.parseDouble(list1_itemPrice.get(i).getText().substring(1)));
		}
		return list_prices;
	}

	public void addModifier() {
		driver.findElement(saladAdd).click();
		driver.findElements(addBtn).get(1).click();
	}
	

	public void addSpecialRequest(String request) {
		driver.findElement(specialRequest).click();
		driver.findElement(specialRequestField).sendKeys(request);
		driver.findElement(requestDoneBtn).click();
		driver.findElement(donePagBtn).click();
		getLogger().info("Waiting For Special Request to display");
		waitForElementPresent(modifier, 5);

	}

	public String editModifier() {
		getLogger().info("Editing Modifier");
		driver.findElement(saladAdd).click();
		waitForElementVisibility(saladAdd,5).click();
		driver.findElements(addBtn).get(2).click();
		return waitForElementVisibility(modifier, 5).getText().substring(3);
	}
	public void editModifierinQSR() {
		getLogger().info("Editing Modifier");
		driver.findElement(noModifier).click();
		driver.findElement(donePagBtn).click();
	}

	public String getTicketId() {
		return driver.findElement(ticketNumber).getText().substring(7);
	}

	public int getItemsCount() {
		return driver.findElements(item).size();
	}

	public double getTotalAmount() {
		return Double.parseDouble(driver.findElement(totalAmount).getText().substring(1));
	}
	
	public boolean waitForItemsCount(int count) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		return wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return driver.findElements(item).size() == count;
			}
		});
	}


	public boolean sendToKDS() {
		getLogger().info("Sending to KDS");
		boolean isClicked = false;
		int count = 0;
		while (!isClicked && count < 5) {
			waitForElementVisibility(sendBtn, 5).click();
			getLogger().info("Waiting for table layout");
			if (waitForElementsVisibility(tables, 5).size() > 0) {
				isClicked = true;
			}
			count++;
		}
		return isClicked;
	}

	public PaymentPage payByCash() {
		boolean isClicked=false;
		int count=0;
		while(!isClicked&&count<5) {
			driver.findElement(payCashBtn).click();
			if(driver.findElements(exactAmount).size()>0) {
				isClicked=true;
			}
			count++;
		}
		return new PaymentPage(driver);
	}
	
	public void removeFirstTime() {
		driver.findElement(removeBtn).click();
	}
	
	public void waitForRemoveInvisible() {
		waitForElementInvisibility(removeBtn,3);
	}

	public boolean removeItem() {
		getLogger().info("Removing Item");
		driver.findElement(removeBtn).click();
		boolean isRemoved=false;
		if(driver.findElements(addedItem).size()==0) {
			isRemoved=true;
		}
		return isRemoved;
	}

	public void voidTicket() {
		getLogger().info("Voiding Order");
		driver.findElement(voidOrderBtn).click();
		driver.findElement(trainingOption).click();
		driver.findElement(voidBtn).click();

	}

	public void clickVoid() {
		driver.findElement(voidOrderBtn).click();
	}

	public void waitForTables() {
		getLogger().info("Waiting For Table Screen to Display");
		waitForElementPresent(tables, 5);
	}

	public double getFirstItemPrice() {
		return Double.parseDouble(driver.findElements(itemsAmount).get(1).getText().substring(1));
	}

	public double getSecondItemPrice() {
		return Double.parseDouble(driver.findElements(itemsAmount).get(2).getText().substring(1));
	}

	public double voidItem() {
		getLogger().info("Voiding Item");
		driver.findElements(item).get(0).click();
		// driver.findElement(item).click();
		driver.findElement(continueBtn).click();
		driver.findElement(voidBtn).click();

		getLogger().info("Verifying Item Voided");
		return Double.parseDouble(driver.findElement(totalAmount).getText().substring(1));
	}

	public boolean verifyTicketHeld() {
		driver.findElement(holdBtn).click();

		return (driver.findElement(heldStatus).isDisplayed() && driver.findElement(activityStatus).isDisplayed());
	}
	
//	public void clickItem() {
//		getLogger().info("Holding Items");
//		driver.findElement(item).click();
//	}
	public void clickItemHolded() {
		getLogger().info("Holding Items");
		driver.findElement(item).click();
		driver.findElement(holdBtn).click();
		
		if (driver.findElements(tables).size() > 0){
			((WebElement) driver.findElements(tables)).click();
		}
	}

	public boolean verifyItemHeld() {
		getLogger().info("Holding Items");
		boolean isClicked = false;
		int count = 0;
		while (!isClicked && count < 5) {
			driver.findElement(holdBtn).click();
			getLogger().info("Waiting For Held Status");
			if (driver.findElements(tables).size() > 0) {
				isClicked = true;
			}
			count++;
		}
		return isClicked;
	}

	/*
	 * Increments the quantity of Item
	 */
	public Boolean addItemAfter() {
		getLogger().info("Adding Quantity");
		driver.findElement(item).click();
		driver.findElement(continueBtn).click();
		driver.findElement(addBtn).click();

		driver.findElement(donePagBtn).click();

		getLogger().info("Waiting For Quantity to be 2");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		return wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return waitForElementVisibility(quantityLabel, 5).getText().equals("2.00");
			}
		});
	}

	public List<String> getItemNames() {
		List<WebElement> list_items = driver.findElements(item);
		List<String> list_itemText = new ArrayList<String>();
		for (WebElement item : list_items) {
			list_itemText.add(item.getText());
		}
		return list_itemText;
	}

	public SplitPage splitTicket() {
		getLogger().info("Clicking split ticket button");
		boolean isClicked = false;
		int count = 0;
		while (!isClicked && count < 5) {
			driver.findElement(splitTicketBtn).click();
			if (driver.findElements(splitTicketScreen).size() > 0) {
				isClicked = true;
			}
			count++;
		}
		return new SplitPage(driver);
	}

	public String getFirstCategoryName() {
		return driver.findElements(level1).get(0).getText();
	}

	public double getSubTotalAmount() {
		getLogger().info("Getting total Amount");
		return Double.parseDouble(driver.findElement(subTotalAmount).getText().substring(1));
	}

	/*
	 * Gets number of tickets in order
	 */
	
	public void clickServiceCharges() {
		driver.findElement(serviceCharges).click();
	}
	public void clickManualServiceCharge() {
		driver.findElement(manualSc).click();
	}
	
	public void clickDone() {
		driver.findElement(btnDone).click();
	}
	public String enterSearchTerm(String searchTerm) {
		WebElement search=waitForElementVisibility(searchField,5);
		search.click(); 
		search.sendKeys(searchTerm); 
	    return searchTerm;
	}

	public boolean verifySearchResultInList(String searchTerm) {
		List<WebElement> searchResults = waitForElementsVisibility(searchedItem, 10);
		for (WebElement result : searchResults) {
	        String resultText = result.getText();
	        System.out.println("Result Text: " + resultText); 
	        if (resultText.toLowerCase().contains(searchTerm.toLowerCase())) { 
	            return true;  
	        }
	    }
	    return false;
	}
	public int getTicketCount() {
		getLogger().info("Getting no. of tickets after split");
		return driver.findElements(ticketsNum).size();
	}

	public double getServiceCharges() {
		return Double.parseDouble(driver.findElement(serviceCharges).getText().substring(1));
	}

	public boolean waitForZeroServiceCharge() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		return wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return Double.parseDouble(waitForElementVisibility(serviceCharges, 5).getText()) == 0;
			}
		});
	}

	public boolean verifyNoteDisplayed(String note) {
		getLogger().info("Adding Note");
		driver.findElement(editBtn).click();
		driver.findElement(noteField).sendKeys(note);
		driver.findElement(doneBtn).click();

		getLogger().info("Getting Note Text");
		return driver.findElement(noteLabel).isDisplayed();
	}

	public void editNote(String note) {
		driver.findElement(editBtn).click();
		driver.findElement(noteField).clear();
		driver.findElement(noteField).sendKeys(note);
		driver.findElement(doneBtn).click();

	}

	public void addPercentCheckDiscount() {
		getLogger().info("Adding Open Percentage discount");
		driver.findElement(discountBtn).click();
		driver.findElement(openPercentCheckBtn).click();
		driver.findElement(btn1).click();
		WebElement zeroBtn = driver.findElement(btn0);
		for (int i = 0; i < 3; i++) {
			zeroBtn.click();
		}
		driver.findElement(numpadDoneBtn).click();
		driver.findElement(doneBtn).click();

	}

	public void addDollarsCheckDiscount() {
		getLogger().info("Adding dollars Amount");
		driver.findElement(discountBtn).click();
		driver.findElement(openDollarsCheckBtn).click();
		driver.findElement(btn1).click();
		WebElement zeroBtn = driver.findElement(btn0);
		for (int i = 0; i < 3; i++) {
			zeroBtn.click();
		}
		driver.findElement(numpadDoneBtn).click();
		driver.findElement(doneBtn).click();

	}

	public void addManagerCheckDiscount() {
		getLogger().info("Adding dollars Amount");
		driver.findElement(discountBtn).click();
		driver.findElement(managerDiscountCheckBtn).click();
		driver.findElement(doneBtn).click();

	}
	
	public void clickCancel() {
		driver.findElement(cancelBtn).click();
	}

	public void waitForDisount() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return Double.parseDouble(driver.findElement(discountAmount).getText().substring(2)) > 0;
			}
		});
	}
	public String getItemDisount() {
		String discount=driver.findElement(discountAmount).getText().substring(2);
		return discount;
	}

	public void selectTaxExemption() {
		getLogger().info("Checking Tax Exemption");
		driver.findElement(taxCheckbox).click();
	}
	public void discountBtn() {
		driver.findElement(discountBtn).click();
	}
	public boolean isManagerApprovalPinScreen() {
		return driver.findElement(managerApproval).isDisplayed();
	}

	public void sendAndProceed() {
		getLogger().info("Sending and Proceeding");
		boolean isClicked = false;
		int count = 0;
		while (!isClicked && count < 5) {
			waitForElementVisibility(payBtn, 5).click();
			if (driver.findElements(sendAndProceedBtn).size() > 0) {
				isClicked = true;
			}
			count++;
		}
		driver.findElement(sendAndProceedBtn).click();
	}
	
	public void waitForOrderScreenToDisappear() {
		waitForElementInvisibility(orderScreen,5);
	}

	public double getBalanceDuePayment() {
		return Double.parseDouble(driver.findElement(balanceDuePayment).getText().substring(1));
	}

	public double getTotal() {
		return Double.parseDouble(driver.findElement(total).getText().substring(1));
	}

	public double getTax() {
		return Double.parseDouble(driver.findElement(totalTaxAmount).getText().substring(1));
	}

	public PaymentPage getPaymentPage() {
		getLogger().info("Opening Payment Screen");
		driver.findElement(payCashBtn).click();

		return new PaymentPage(driver);
	}

	public void splitPaymentAmount() {
		waitForElementToBeClickable(incrementBtn,5).click();
	}

	public double getPaymentAmount() {
		return Double.parseDouble(driver.findElement(paymentAmount).getText());
	}

	public double getRemainingAmount() {
		return Double.parseDouble(driver.findElement(remainingAmount).getText().substring(18));
	}

	public String getBillStatus() {
		return waitForElementVisibility(billStatus, 5).getText();
	}

	public void clickChangeTable() {
		getLogger().info("Tapping Change Table Button");
		driver.findElement(changeTableBtn).click();
	}

	public int getGuestsCount() {
		WebElement guests = driver.findElement(guestCount);
		return guests.findElements(By.className("android.view.ViewGroup")).size();
	}

	public void openNextTicket(int index, String ticketId) {
		driver.findElements(ticketsNum).get(index).click();

		waitForElementPresent(By.xpath("//android.widget.TextView[@text='Ticket " + ticketId + "']"), 3);
	}

	public String getTotalCalculated(double subTotal) {
		double serviceCharge = Math.round((subTotal * 0.25) * 100.0) / 100.0;
		double tax = Math.round((subTotal * 11.72) * 100.0) / 100.0;
		return Double.toString(subTotal + serviceCharge + tax);
	}

	public void waitForStay() {
		driver.findElement(stayBtn).click();

		waitForElementPresent(activityStatus, 5);
	}
	
	public void addModifierTill() {
		boolean isModifierAdded=false;
		int count=0;
		while(!isModifierAdded&&count<5) {
			waitForElementToBeClickable(item, 5).click();
			addModifier();
			driver.findElement(donePagBtn).click();
			if(driver.findElements(modifier).size()>0) {
				isModifierAdded=true;
			}
			count++;
		}
	}

	public void addItemWithModifier() {
		driver.findElement(By.xpath("//android.widget.TextView[@text='Salads']")).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='Caesar']")).click();
	}
	
	
	public void increaseQuantityTill() {
		boolean isIncreased=false;
		int count=0;
		while(!isIncreased&&count<5) {
			waitForElementToBeClickable(item, 5).click();
			addMultiple(1);
			driver.findElement(donePagBtn).click();
			WebElement quantity=waitForElementVisibility(quantityLabel,5);
			if(Double.parseDouble(quantity.getText())==2) {
				isIncreased=true;
			}
			count++;
		}
	}
	

	public void editItem(int index) {
		getLogger().info("Clicking Added Item");
		waitForElementToBeClickable(item, 5);
		driver.findElement(item).click();

	}
	
//	public void editItem1(int index) {
//		getLogger().info("Clicking Added Item");
//		driver.findElement(item).click();
//
//	}

	public void confirmEdit() {
		driver.findElement(continueBtn).click();
	}

	public void voidSelectedItem() {
		driver.findElement(voidBtn).click();
	}

	public void waitForMenu() {
		waitForElementVisibility(menuView,5);
	}

	public void editSpecialRequest(String request) {
		getLogger().info("Editing Special Request");
		driver.findElement(specialRequest).click();
		driver.findElement(specialRequest).click();
		WebElement specialRequestEdit = driver.findElement(specialRequestField);
		specialRequestEdit.clear();
		specialRequestEdit.sendKeys(request);
		driver.findElement(requestDoneBtn).click();
		driver.findElement(donePagBtn).click();

	}
	
	public void selectPercentComp() {
		getLogger().info("Select Percentage Comp Items");
		driver.findElement(itemComps).click();
		driver.findElement(selectPercent).click();
	}
	public void selectDollarComp() {
		getLogger().info("Select Dollar Comp Items");
		driver.findElement(itemComps).click();
		driver.findElement(selectDollar).click();
	}
	public void enterCompDiscount() {
		getLogger().info("enter Comp Discount");
		waitForElementVisibility(numppad,5).isDisplayed();
		driver.findElement(btn1).click();
		WebElement zeroBtn = driver.findElement(btn0);
		for (int i = 0; i < 3; i++) {
			zeroBtn.click();
		}
		driver.findElement(numpadDoneBtn).click();
	}

	public String getModifierText() {
		return driver.findElement(modifier).getText();
	}

	public boolean isMessageDisplayed() {
		return driver.findElement(confirmationMessage).isDisplayed();
	}

	public void stayHere() {
		driver.findElement(stayBtn).click();

	}
	
	public int getItemCount() {
		List <WebElement> element= driver.findElements(item);
		return element.size();
	}
	
	public boolean isModifierAdded() {
		getLogger().info("Checking custom Item");
		waitForElementVisibility(item, 15).isDisplayed();
		return true;
	}

	public void addCustomItem(String itemName) {
		getLogger().info("Adding Custom Item");
		driver.findElement(customItem).click();

		driver.findElement(noteField).sendKeys(itemName);
		driver.findElement(customItemPrice).click();
		driver.findElement(btn1).click();
		WebElement zeroBtn = driver.findElement(btn0);
		for (int i = 0; i < 3; i++) {
			zeroBtn.click();
		}
		driver.findElement(customAddBtn).click();

	}

	public void openKebabMenu() {
		boolean isDisplayed=false;
		int count=0;
		while(!isDisplayed&&count<5) {
			driver.findElement(threeDotsBtn).click();
			if(driver.findElements(optionsList).size()>0) {
				isDisplayed=true;
			}
		}
	}

	public void editGuests() {
		driver.findElement(guestsBtn).click();

	}

	public void increaseGuests(String num) {
		getLogger().info("Increasing Guests");
		driver.findElement(numpadClearBtn).click();
		driver.findElement(By.xpath("//android.widget.TextView[@text='" + num + "']")).click();
		driver.findElement(numpadDoneBtn).click();

	}

	public int getQuantity() {
		String quantityText = driver.findElement(quantityLabel).getText();
		String[] quantity = quantityText.split("\\.");
		return Integer.parseInt(quantity[0]);
	}
	
	public boolean waitForQuantity(double count) {
		return new WebDriverWait(driver, Duration.ofSeconds(5))
				.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver driver) {
						return Double.parseDouble(waitForElementVisibility(quantityLabel,5).getText()) == (count);
					}
				});
	}

	public void updateAfterSent() {
		waitForElementVisibility(donePagBtn,10).click();
	}
	public void doneBtn() {
		waitForElementVisibility(donePagBtn,5).click();
	}


	public boolean waitForModifierAdded() {
		return waitForElementVisibility(modifier, 10).isDisplayed();
	}

	public void waitForPaymentTicket() {
		getLogger().info("Waiting For Payment Screen");
		new WebDriverWait(driver, Duration.ofSeconds(10))
		.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return Double.parseDouble(driver.findElement(total).getText().substring(1))>0;
			}
		});
	}

	public void goBackToMenu() {
		getLogger().info("Going Back to Menu Screen");
		driver.findElement(paymentBackBtn).click();

	}

	public void waitForServiceCharge(String amount) {
		new WebDriverWait(driver, Duration.ofSeconds(10))
				.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver driver) {
						return driver.findElement(serviceCharges).getText().equals("$" + amount);
					}
				});
	}
	
	public void waitForServiceCharge() {
		new WebDriverWait(driver, Duration.ofSeconds(10))
				.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver driver) {
						return Double.parseDouble(driver.findElement(serviceCharges).getText().substring(1))>0;
					}
				});
	}

	public double getBalanceDue() {
		return Double.parseDouble(driver.findElement(balanceDue).getText().substring(1));
	}

	public double getServiceChargesPayment() {
		return Double.parseDouble(driver.findElement(serviceChargesPayment).getText().substring(1));
	}

	public double getSubTotalPayment() {
		return Double.parseDouble(driver.findElement(subTotalPayment).getText().substring(1));
	}

	public double getTaxPayment() {
		return Double.parseDouble(driver.findElement(taxPayment).getText().substring(1));
	}

	public double getBalanceDueLeft() {
		return Double.parseDouble(driver.findElement(balanceDueLeft).getText().substring(1));
	}

	public double getTotalPayment() {
		return Double.parseDouble(driver.findElement(total).getText().substring(1));
	}

	public boolean isMenuDisplayed() {
		return waitForElementVisibility(category, 5).isDisplayed();
	}

	public String serverChange(String name) {
		driver.findElement(changeServerBtn).click();
		List<WebElement> servers = driver.findElements(serverName);
		for (WebElement server : servers) {
			if (!server.getText().equals(name)) {
				server.click();
				return server.getText();
			}
		}
		return "No other Server";
	}

	public void changeServe() {
		driver.findElement(changeServerBtn).click();
	}

	public String changeServerName(String serverName) {
	    List<WebElement> servers = driver.findElements(serverRow);
	    int removeIndex = -1;
	    for (int i = 0; i < servers.size(); i++) {
	        if (servers.get(i).getText().contains(serverName)) {
	            removeIndex = i;  // Save the index of the matching server
	            break;  // Stop after finding the first match
	        }
	    }

	    if (removeIndex != -1) {
	        servers.remove(removeIndex);
	    }
	    String text = servers.get(0).getText();
	    servers.get(0).click();
//	    int spaceIndex = text.indexOf(" ");
//	    if (spaceIndex != -1) {
//	        text = text.substring(0, spaceIndex);
//	    }
//	    servers.get(0).getText().split(text);
	    return text;
	}
	
	public void clickTakeoutBtn() {
		driver.findElement(takeoutBtn).click();
	}



	public void change() {
		driver.findElement(changeBtn).click();
	}

	public int getModifierCount() {
		return driver.findElements(modifier).size();
	}

	public void goBackFromOrder() {
		driver.findElement(backBtn).click();

	}

	public void discardChanges() {
		driver.findElement(discardBtn).click();
	}

	public void selectModifierGroup() {
		driver.findElement(modifierGroup2).click();
	}

	public void selectInfoIcon() {
		driver.findElement(infoIcon).click();
	}

	public boolean isItemDetailPopupDisplayed() {
		return driver.findElement(itemDetailPopup).isDisplayed();
	}

	public void closeItemDetailPopup() {
		driver.findElement(closeIcon).click();
	}

	public void addFirstItem() {
		driver.findElements(level2).get(0).click();
	}

	public void addTenItems() {
		driver.findElements(level2).get(0).click();
		driver.findElements(level2).get(1).click();
		driver.findElements(level2).get(2).click();
		driver.findElements(level2).get(3).click();
		driver.findElements(level2).get(4).click();
		driver.findElements(level2).get(5).click();
		driver.findElements(level2).get(6).click();
		driver.findElements(level2).get(7).click();
		driver.findElements(level2).get(8).click();
		driver.findElements(level2).get(9).click();
	}

	public PaymentPage tapOnCard() {
		driver.findElement(cardBtn).click();
		return new PaymentPage(driver);
	}

	public void clickGuestChip(int index) {
		WebElement guestsView = driver.findElement(guestCount);
		guestsView.findElements(By.className("android.view.ViewGroup")).get(index).click();
	}

	public boolean isVoidOptions() {
		return driver.findElements(trainingOption).size() == 0;
	}

	public double getModifierPrice() {
		return Double.parseDouble(driver.findElement(modifierPrice).getText().substring(1));
	}

	public void editPaymentAmount() {
		driver.findElement(backspaceBtn).click();
	}

	public void openServiceCharge() {
		driver.findElement(serviceCharges).click();
	}

	public void uncheckAutoGratuity() {
		driver.findElement(autoGratuity).click();
	}

	public void uncheckAllServiceCharges() {
		WebElement serviceCharges = driver.findElement(serviceChargesList);
		List<WebElement> listServiceCharge = serviceCharges.findElements(By.className("android.view.ViewGroup"));
		if (listServiceCharge.size() > 1) {
	        listServiceCharge.get(0).click();  
	        listServiceCharge.get(1).click();  
	    }
	}

	public void clickGuestChips(int index) {
		driver.findElements(guests).get(index).click();
	}

	public void increaseModifierQuantity() {
//		driver.findElement(saladAdd).click();
		driver.findElement(saladAdd).click();
		driver.findElement(saladAdd).click();
		driver.findElements(addBtn).get(1).click();
	}

	public double getQunatityModifierPrice() {
		return Double.parseDouble(driver.findElement(modifierPrice).getText().substring(1));
	}

	public char getModifierQuantity() {
		return driver.findElement(modifier).getText().charAt(0);
	}
	
	
	public void cancelTill() {
		boolean isCanceled=false;
		int count=0;
		while(!isCanceled&&count<5) {
			driver.findElement(cancelBtn).click();
			if(getItemsCount()==0) {
				isCanceled=true;
			}
			count++;
		}
		List<WebElement> listNoBtn=driver.findElements(noBtn);
		if(listNoBtn.size()>0) {
			listNoBtn.get(0).click();
		}
	}

	public void updateModifier() {
		getLogger().info("Update Quantity");
		driver.findElement(updateModifiersBtn).click();
	}

	public void tapTenderButton() {
		getLogger().info("Clicking Tender Button");
		boolean isDisplayed=false;
		int count=0;
		while(!isDisplayed&&count<5) {
			driver.findElement(payTenderBtn).click();
			if(driver.findElements(serverWithoutTip).size()>0) {
				isDisplayed=true;
			}
			count++;
		}
		
	}

	public PaymentPage tapServerWithoutTip() {
		getLogger().info("Clicking Server Without Tip Button");
		driver.findElement(serverWithoutTip).click();
		return new PaymentPage(driver); // Return the current instance
	}

//	public boolean waitForSubtotal(String amount) {
//		new WebDriverWait(driver,Duration.ofSeconds(5)).until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
//			public Boolean apply(WebDriver driver)  
//	        {
//	        	return driver.findElement(subTotalAmount).getText().substring(1).equals(amount);
//	        }
//		});
//	}

	public boolean waitForSubtotal(String amount) {
		return new WebDriverWait(driver, Duration.ofSeconds(5))
				.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver driver) {
						return driver.findElement(subTotalAmount).getText().substring(1).equals(amount);
					}
				});
	}
	
	public boolean waitForSubtotal(double amount) {
		return new WebDriverWait(driver, Duration.ofSeconds(5))
				.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver driver) {
						return Double.parseDouble(driver.findElement(subTotalAmount).getText().substring(1))==amount;
					}
				});
	}

	public void clickAddCustomer() {
		driver.findElement(clickCustomer).click();
	}

	public void clickNewCustomer() {
		driver.findElement(newCustomer).click();
	}

	public String enterFirstName() {
		String[] names = { "John", "Alice", "Emma", "Michael", "Sophia", "James", "Olivia", "David", "Ava", "William" };
		Random random = new Random();
		String randomName = names[random.nextInt(names.length)];
		driver.findElement(nameFirst).click();
		driver.findElement(nameFirst).sendKeys(randomName);
		return randomName;
	}

	public String enterLastName() {
		String[] names = { "Smith", "Johnson", "Brown", "Williams", "Jones", "Miller", "Davis", "Garcia", "Wilson",
				"Anderson" };
		Random random = new Random();
		String randomName = names[random.nextInt(names.length)];
		driver.findElement(nameLast).click();
		driver.findElement(nameLast).sendKeys(randomName);
		return randomName;
	}
//	public void phone(String phoneNumber) {
//	    WebElement phoneField = driver.findElement(this.phoneNumber);
//	    phoneField.sendKeys(phoneNumber);
//	}

	public String enterPhone(String number) {
		String generatedPhoneNumber = generateValidPhoneNumber();
		WebElement getNumber = driver.findElement(phoneNumber);
		getNumber.sendKeys(generatedPhoneNumber);
		return generatedPhoneNumber;
	}

	public void clickSave() {
		driver.findElement(btnSave).click();
	}

	public boolean confirmName() {
		WebElement moreElement = waitForElementVisibility(customerName, 20);
		return moreElement.isDisplayed();

	}

	public void clickCustomer() {
		driver.findElement(customerName).click();
	}
	
	public List<String> getCustomers(){
		List<WebElement> listCustomer= driver.findElements(listCustomerName);
		List<String> listCustomerName=new ArrayList<String>();
		for(WebElement customer:listCustomer) {
			listCustomerName.add(customer.getText());
		}
		for(String customerName:listCustomerName) {
			System.out.println("Customer: "+customerName);
		}
		return listCustomerName;
	}

	public boolean isCustomerInList(String firstName) {
		List<WebElement> listCustomer = driver.findElements(listCustomerName);
		for (WebElement customer : listCustomer) {
			String customerText = customer.getText();
			if (customerText.contains(firstName)) {
				return true;
			}
		}
		try {
			WebElement customer = scrollToById(firstName,"rvCustomers");
			return customer != null && customer.getText().contains(firstName);
		} catch (NoSuchElementException e) {
			return false;
		}
	}

}