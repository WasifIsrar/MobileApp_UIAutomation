package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import basePage.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class MposOrderPage extends BasePage{
	
	private By level1=By.xpath("(//androidx.recyclerview.widget.RecyclerView)[1]/android.view.ViewGroup");
	private By level2=By.xpath("(//androidx.recyclerview.widget.RecyclerView)[2]/android.view.ViewGroup");
	private By itemDoneBtn=By.id("btDoneItem");
	private By ticketItems=By.xpath("//androidx.recyclerview.widget.RecyclerView[contains(@resource-id,'rvTicketItems')]/android.view.ViewGroup");
	private By payCashBtn=By.id("ivCash");
	private By suggestionAmount=By.id("suggestionText");
	private By noReceiptBtn=By.id("clNoReceipt");
	private By excellentReview=By.id("ivExcellent");
	private By closeBtn=By.id("ivClose");
	private By menu=By.id("tvMenuItem");
	private By paymentScreen=By.id("clPayment");
	private By balanceDue=By.id("tvBalanceDue");
	private By completePaymentBtn=By.id("btnCompletePayment");
	private By subtotal=By.id("tvSubtotal");
	private By serviceCharge=By.id("tvServiceCharge");
	private By tax=By.id("tvTax");
	private By total=By.id("tvTotal");
	private By menuCollapseBtn=By.id("ivMenuCollapse");
	private By menuExpandBtn=By.id("ivMenuExpand");
	private By kebabMenu=By.id("ivKebabMenu");
	private By orderKebabMenu=By.id("ivKebab");
	private By taxExemptionCheckbox=By.id("checkBoxTaxExcemption");
	private By dealsDiscountBtn=By.id("tvDealsDiscount");
	private By applyDiscountBtn=By.id("tvApplyDiscount");
	private By applyBtn=By.id("btnApply");
	private By employeeDiscount=By.xpath("//android.widget.TextView[contains(@text,'Employee Discount')]");
	private By openPercentDiscount=By.xpath("//android.widget.TextView[@text='Open % Check']");
	private By openDollarDiscount=By.xpath("//android.widget.TextView[@text='Open $ Check']");
	private By managerDiscount=By.xpath("//android.widget.TextView[contains(@text,'Manager')]");
	private By discountAmount=By.id("tvDiscount");
	private By subtotalAfterDiscount=By.id("tvSubtotalAfterDiscountAmount");
	private By tickBtn=By.id("clTick");
	private By kebabVoidBtn=By.id("tvKebabVoid");
	private By voidOrderBtn=By.id("btnVoidOrder");
	private By reasonTwo=By.id("rbReasonTwo");
	private By reasonVoidBtn=By.id("btnVoidOrderReason");
	private By splitBtn=By.id("ivSplit");
	private By amount=By.id("tvAmount");
	private By plusBtn=By.id("ivPlus");
	private By paymentAmount=By.id("tvPaymentAmount");
	private By totalAmount=By.id("tvTotalAmount");
	private By otherOptionsBtn=By.id("tvOther");
	private By ticketPlusBtn=By.id("ClPlus");
	private By splitItem=By.id("Clitem");
	private By splitTicket=By.id("rvItem");
	private By splitItemBtn=By.id("tvSplitByWays");
	private By ticketStatus=By.id("ivPartial");
	private By editServiceChargeBtn=By.id("tvEditServiceCharges");
	private By caLiving=By.xpath("//android.widget.CheckBox[@text='CA Living (5.0%)']");
	private By saveBtn=By.id("btnSave");
	private By serviceCharges=By.xpath("//androidx.recyclerview.widget.RecyclerView[contains(@resource-id,'rvCharges')]/android.view.ViewGroup");
	private By ticketNumber=By.id("tvTicketNo");
	private By addNoteBtn=By.id("tvKebabAddNote");
	private By noteField=By.id("etAddNote");
	private By saveNoteBtn=By.id("btnNoteSave");
	private By tableNumber=By.id("tvTableNo");
	private By itemName=By.id("tvItemName");
	private By item=By.id("llItemName");
	private By continueBtn=By.id("btnContinue");
	private By addQuantityBtn=By.id("ivPlus");
	private By itemQuantity=By.id("tvItemQty");
	private By holdBtn=By.id("btHold");
	private By voidItemBtn=By.id("btnVoidItem");
	private By voidItem=By.className("android.widget.CheckBox");
	private By voidCheckBtn=By.id("btnVoidItemAfterCheck");
	private By splitGuestBtn=By.id("tvSplitByGuests");
	private By tickSplitBtn=By.id("ivTick");
	private By splitRightArrow=By.id("ivRightArrow");
	private By paymentPager=By.id("clPaymentPager");
	private By splitTicketNumber=By.id("tvTicketNum");
	private By customItem=By.id("tvKebabAddCustomItem");
	private By customItemName=By.id("etItemName");
	private By customItemPrice=By.id("etItemPrice");
	private By addCustomBtn=By.id("btnChange");
	private By numpadBackBtn=By.id("btn_backspace");
	private By cancelTicketBtn=By.id("tvKebabCancelTicket");
	private By yesBtn=By.id("btnYes");
	private By guestNumber = By.id("tvGuestNumber");
	private By guestLogo = By.id("llGuestLogo");
	private By editGuest = By.id("tvEditGuestCount");
	private By addGuestButton = By.id("ivPlus");
	private By doneBtn = By.id("btnDone");
	private By addGuestInfo = By.id("tvAddGuestInfo");
	private By newGuest = By.id("tvNewGuest");
	private By firstName = By.id("edFirstName");
	private By lastName = By.id("edLastName");
	private By phoneNumber = By.id("edNumber");
	private By newGuestPopUp = By.className("android.widget.Toast");
	private By guestNames = By.id("tvGuestName");
	private By changeServer = By.id("tvKebabChangeServer");
	private By currentServer = By.id("tvCurrent");
	private By serverName = By.id("rbServer");
	private By changeServerBtn = By.id("btnChange");
	private By selectServerName = By.id("tvServerName");
	private By payByTender = By.id("tvPayByTender");
	private By serverWithTip = By.xpath("//android.widget.TextView[@text='Server with Tip']");
	private By managerWithoutTip = By.xpath("//android.widget.TextView[@text='Manager without Tip']");
	private By addATip = By.xpath("//android.widget.TextView[@text='Add a Tip']");
	private By menuDisplay = By.id("llBtnsTab");
	private By manualServiceCharge = By.xpath("//android.widget.CheckBox[contains(@text, 'Manual')]");
	private By managerApproval = By.xpath("//android.widget.TextView[@text='Please enter manager’s PIN']");
	private By search = By.id("ivSearchIcon");
	private By searchBar = By.id("clSFEtSearch");
	private By searchedItems = By.id("tvMenuItem");
	private By balanceDueText = By.xpath("//android.widget.TextView[contains(@text, 'Balance Due')]");
	private By discountAppliedMessage = By.id("tvMessage");
	private By specialRequest = By.id("itemView");
	private By addSpecialRequest = By.id("etSpecialRequest");
	private By srDoneBtn = By.id("btnSRDone");
	private By specialReqText = By.id("tvModifier");
	private By itemNameText = By.id("itemNameTV");
	private By thanksMessage = By.id("tvThanks");
    private By paymentTab = By.id("tvPayment");
	private By customerName = By.id("customerNameTV");
	private By totalItems = By.xpath("//androidx.recyclerview.widget.RecyclerView[contains(@resource-id, 'rvTicketItems')]//android.view.ViewGroup[contains(@resource-id, 'parentLayout')]");
	private By aroundTheWorld = By.xpath("//android.widget.TextView[@text='Around The World']");
	private By applyOnItems = By.id("tvApplyItem");
	private By openPercent = By.xpath("//android.widget.TextView[contains(@resource-id, 'tvDiscountName') and @text='Open %']");
	private By backBtn = By.id("ivBack");
	private By menuItemName = By.xpath("//android.widget.TextView[contains(@resource-id, 'tvItemName') and contains(@text, 'Around the World Flight')]");
	private By modifierText = By.id("tvModifier");
	private By saladItemName = By.xpath("//android.widget.TextView[contains(@resource-id, 'tvItemName') and contains(@text, 'Caesar Salad')]");
	private By openDollar = By.xpath("//android.widget.TextView[contains(@resource-id, 'tvDiscountName') and @text='Open $']");

	public MposOrderPage(AndroidDriver driver) {
		super(driver);
	}

	public void isOrderPageDisplayed() {
		waitForElementVisibility(menuDisplay, 10);
		List<WebElement> menuDisp = driver.findElements(menu);
		if(menuDisp.size() == 0)
		{
			collapseMenu();
			expandMenu();
		}
	}
	
	public void addItem() {
		driver.findElements(level1).get(0).click();
		driver.findElements(level2).get(0).click();
	}
	
	public void addSecondItem() {
		driver.findElements(level1).get(0).click();
		driver.findElements(level2).get(1).click();
	}
	
	public void addItemWithModifier() {
		driver.findElement(By.xpath("//android.widget.TextView[contains(@resource-id, 'tvMenuItem') and @text='Salads']")).click();
		driver.findElements(level2).get(0).click();
	}
	
	public void doneItem() {
		driver.findElement(itemDoneBtn).click();
	}
	
	public void isItemAdded() {
		waitForElementVisibility(ticketItems,5);
	}
	
	public void payByCash() {
		System.out.println("Paying By Cash");
		driver.findElement(payCashBtn).click();
	}
	
	public void balanceDueText() {
		waitForElementVisibility(balanceDueText, 10);
	}
	
	public void selectExactAmount() {
		driver.findElements(suggestionAmount).get(0).click();
		driver.findElement(doneBtn).click();
	}
	
	public void waitForReceiptModal() {
		waitForElementVisibility(cancelBtn, 7);
	}
	
	public void noReceipt() {
		System.out.println("Clicking no receipt Button");
		driver.findElement(noReceiptBtn).click();
	}
	
	public void giveExcellentReview() {
		System.out.println("Clicking Review");
		driver.findElement(excellentReview).click();
	}
	
	public void closeThanksScreen() {
		System.out.println("Closing Thanks Screen");
		driver.findElement(closeBtn).click();
	}
	
	public void waitForPaymentScreen() {
		waitForElementVisibility(paymentScreen,5);
	}
	
	public void getDiscountAppliedMessage() {
		waitForElementInvisibility(discountAppliedMessage, 5);
	}
	
	public void itemNameVisible() {
		waitForElementVisibility(itemNameText, 7);
	}
	
	public void splitTicket() {
		System.out.println("Splitting Amount");
		driver.findElement(splitBtn).click();
	}
	
	public Double getAmount() {
		return Double.parseDouble(driver.findElement(amount).getText());
	}
	
	public double getBalanceDue() {
		return Double.parseDouble(driver.findElement(balanceDue).getText().substring(1));
	}
	
	public void tapPlus() {
		driver.findElement(plusBtn).click();
	}
	
	public void waitForBalanceDue() {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
				return Double.parseDouble(driver.findElement(balanceDue).getText().substring(1))>0;
	        }
		});
	}
	
	public void waitForSubtotal() {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
				return Double.parseDouble(driver.findElement(subtotal).getText().substring(1))>0;
	        }
		});
	}
	
	public void waitForZeroBalanceDue() {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
				return Double.parseDouble(driver.findElement(balanceDue).getText().substring(1))==0;
	        }
		});
	}
	
	public void tapCompletePayment() {
		System.out.println("Completing Payment");
		driver.findElement(completePaymentBtn).click();
		
	}
	
	public double getSubtotal() {
		return Double.parseDouble(driver.findElement(subtotal).getText().substring(1));
	}
	
	public double getServiceCharges() {
		return Double.parseDouble(driver.findElement(serviceCharge).getText().substring(1));
	}
	
	public double getTax() {
		return Double.parseDouble(driver.findElement(tax).getText().substring(1));
	}
	
	public double getTotal() {
		return Double.parseDouble(driver.findElement(total).getText().substring(1));
	}
	
	public List<Double> getSplitTotal() {
		List<WebElement> splitTotalElements=driver.findElements(total);
		List<Double> splitTotal=new ArrayList<Double>();
		for(WebElement splitTotalElement:splitTotalElements) {
			splitTotal.add(Double.parseDouble(splitTotalElement.getText().substring(1)));
		}
		return splitTotal;
	}
	
	public void collapseMenu() {
		driver.findElement(menuCollapseBtn).click();
	}
	 
	public void waitForItems(int count) {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver)  
	        {
				return driver.findElements(item).size()>count;
	        }
		});
	}
	
	public boolean isOnOrderPage() {
		return driver.findElements(payBtn).size()>0;
	}
	
	public boolean isOnPaymentPage() {
		return driver.findElements(paymentScreen).size()>0;
	}
	
	public void openKebabMenu() {
		driver.findElement(kebabMenu).click();
	}
	
	public void openOrderKebabMenu() {
		driver.findElement(orderKebabMenu).click();
	}
	
	public void goBack() {
		driver.findElement(backBtn).click();
	}
	
	public void navigateBack() {
		driver.navigate().back();
	}
	
	public void selectTaxExemption() {
		System.out.println("Selecting Tax Exemption");
		driver.findElement(taxExemptionCheckbox).click();
		
	}
	
	public void selectDealsDiscount() {
		System.out.println("Applying Discount");
		waitForElementToBeClickable(dealsDiscountBtn, 10).click();
	}
	
	public void selectApplyDiscount() {
		driver.findElement(applyDiscountBtn).click();
	}
	
	public void applyEmployeeDiscount() {
		driver.findElement(employeeDiscount).click();
	}
	
	public void enterTenDiscount() {
		driver.findElement(btn1).click();
		WebElement zeroBtn=driver.findElement(btn0);
		for(int i=0;i<3;i++) {
			zeroBtn.click();
		}
	}
	
	public void enterTwoDollarDiscount() {
		driver.findElement(btn2).click();
		WebElement zeroBtn=driver.findElement(btn0);
		for(int i=0;i<2;i++) {
			zeroBtn.click();
		}
	}
	
	public void apply() {
		driver.findElement(applyBtn).click();
		
	}
	
	public void applyOpenCheckPercentage() {
		driver.findElement(openPercentDiscount).click();
	}
	
	public void applyOpenCheckDollar() {
		driver.findElement(openDollarDiscount).click();
	}
	
	public void applyManagerDiscount() {
		driver.findElement(managerDiscount).click();
	}
	
	public double getDiscountAmount() {
		return Double.parseDouble(driver.findElement(discountAmount).getText().substring(2));
	}
	
	public double getSubtotalAfterDiscount() {
		return Double.parseDouble(driver.findElement(subtotalAfterDiscount).getText().substring(1));
	}
	
	public void tick() {
		driver.findElement(tickBtn).click();
	}
	
	public void voidOrderItem() {
		driver.findElement(kebabVoidBtn).click();
	}
	
	public void voidWholeOrder() {
		driver.findElement(voidOrderBtn).click();
		driver.findElement(reasonTwo).click();
		driver.findElement(reasonVoidBtn).click();
	}
	
	public Double getPaymentAmount() {
		return Double.parseDouble(driver.findElement(paymentAmount).getText());
	}
	
	public Double getTotalAmount() {
		return Double.parseDouble(driver.findElement(totalAmount).getText().substring(1));
	}
	
	public void tapOtherOptions() {
		driver.findElement(otherOptionsBtn).click();
	}
	
	public void splitItem() {
		System.out.println("Splitting Item");
		driver.findElement(splitItem).click();
		driver.findElement(splitItemBtn).click();
		driver.findElement(plusBtn).click();
		driver.findElement(plusBtn).click();
		driver.findElement(doneBtn).click();
		
	}
	
	public void moveItem() {
		driver.findElement(ticketPlusBtn).click();
		driver.findElement(ticketPlusBtn).click();
		driver.findElement(splitItem).click();
		driver.findElements(splitTicket).get(1).click();
		driver.findElement(splitItem).click();
		driver.findElements(splitTicket).get(2).click();
	}
	
	public boolean isPartial() {
		return driver.findElement(ticketStatus).getText().equals("Partial");
	}
	
	public void editServiceCharge() {
		System.out.println("Editing Service Charge");
		driver.findElement(editServiceChargeBtn).click();
	}
	
	public void uncheckCALiving() {
		driver.findElement(caLiving).click();
	}
	
	public void uncheckAllServiceCharges() {
		List<WebElement> listServiceCharges=driver.findElements(serviceCharges);
		for(int i = 0; i < 2; i++) {
			listServiceCharges.get(i).click();
		}
	}
	
	public void save() {
		driver.findElement(saveBtn).click();
		
	}
	
	public String getTicketId() {
		return driver.findElement(ticketNumber).getText();
	}
	
	public void clickAddNote() {
		System.out.println("Adding Note");
		driver.findElement(addNoteBtn).click();
	}
	
	public void enterNote(String note) {
		driver.findElement(noteField).sendKeys(note);
	}
	
	public void saveNote() {
		driver.findElement(saveNoteBtn).click();
		
	}
	
	public void send() {
		System.out.println("Sending");
		driver.findElement(sendBtn).click();
		
	}
	
	public String getTableNumber() {
		return driver.findElement(tableNumber).getText();
	}
	
	public void editItem() {
		driver.findElement(item).click();
	}
	
	public void addModifier() {
		driver.findElement(By.xpath("//android.widget.TextView[@text='Sal Add ons']")).click();
		driver.findElements(level2).get(0).findElement(By.className("android.view.View")).click();
	}
	
	public void continueEditing() {
		driver.findElement(continueBtn).click();
	}
	
	public void increaseQuantity() {
		driver.findElement(addQuantityBtn).click();
	}
	
	public List<String> getItemNames() {
		List<WebElement> list_items=driver.findElements(itemName);
		list_items.remove(0);
		List<String> list_itemText=new ArrayList<String>();
		for(WebElement item:list_items) {
			list_itemText.add(item.getText());
		}
		return list_itemText;
	}
	
	public boolean waitForQuantity() {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(5));
		return wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>(){
	        public Boolean apply(WebDriver driver)  
	        {
	        	return driver.findElements(itemQuantity).get(1).getText().equals("2.00");
	        }
	    });
	}
	
	public void editModifier() {
		driver.findElements(level2).get(0).findElement(By.className("android.view.View")).click();
		driver.findElements(level2).get(1).findElement(By.className("android.view.View")).click();
	}
	
	public String getModifierText() {
		return driver.findElements(level2).get(1).getText();
	}
	
	public void waitForModifierUpdate(String updatedModifier) {
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(5));
		wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>(){
	        public Boolean apply(WebDriver driver)  
	        {
	        	return driver.findElement(modifier).getText().contains(updatedModifier);
	        }
	    });
	}
	
	public String getModifier() {
		return driver.findElement(modifier).getText().substring(3);
	}
	
	public void expandMenu() {
		waitForElementVisibility(menuExpandBtn,5).click();
	}
	
	public void hold() {
		driver.findElement(holdBtn).click();
	}
	
	public void voidItem() {
		driver.findElement(voidItemBtn).click();
		driver.findElement(voidItem).click();
		driver.findElement(voidCheckBtn).click();
	}
	
	public void splitByGuests() {
		driver.findElement(splitGuestBtn).click();
	}
	
	public void tickSplit() {
		driver.findElement(tickSplitBtn).click();
	}
	
	public void moveRight() {
		driver.findElement(splitRightArrow).click();
	}
	
	public void waitForTicket(String ticketId) {
		waitForElementVisibility(By.xpath("//android.widget.TextView[@text='"+ticketId+"']"), 5);
	}
	
	public void waitForOrderScreen() {
		waitForElementVisibility(paymentPager,10);
	}
	
	public String getSplitTicketNumber() {
		return driver.findElement(splitTicketNumber).getText();
	}
	
	public void addCustomItem() {
		driver.findElement(customItem).click();
		WebElement customItemNameElement=driver.findElement(customItemName);
		customItemNameElement.click();
		customItemNameElement.sendKeys("Custom");
		WebElement customItemPriceElement=driver.findElement(customItemPrice);
		customItemPriceElement.click();
		customItemPriceElement.sendKeys("1000");
		driver.navigate().back();
		driver.findElement(addCustomBtn).click();
	}
	
	public void addCustomAmount() {
		driver.findElement(paymentAmount).click();
		driver.findElement(numpadBackBtn).click();
	}
	
	public void clickCancelTicket() {
		driver.findElement(cancelTicketBtn).click();
	}
	
	public void clickYesBtn() {
		driver.findElement(yesBtn).click();
	}
	
	public int getGuestNumber() {
	    List<WebElement> guest_number = driver.findElements(guestNumber);
	    guest_number = new ArrayList<>(guest_number.subList(2, guest_number.size())); 
	    return guest_number.size();
	}
	
	public void tapGuestLogo() {
		driver.findElement(guestLogo).click();
	}
	
	public void tapEditGuestCount() {
		driver.findElement(editGuest).click();
	}
	
	public void addGuest() {
		driver.findElement(addGuestButton).click();
	}
	
	public void tapOnDoneButton() {
		driver.findElement(doneBtn).click();
	}
	
	public void tapAddGuestInfo() {
		driver.findElement(addGuestInfo).click();
	}
	
	public void tapOnNewGuest() {
		driver.findElement(newGuest).click();
	}
	
	public String enterFirstName() {
	    WebElement firstNameField = driver.findElement(firstName);
	    firstNameField.click();
	    String randomFirstName = generateRandomString(5);
	    firstNameField.sendKeys(randomFirstName);
		return randomFirstName;
	}
	
	public String enterLastName() {
	    WebElement lastNameField = driver.findElement(lastName);
	    lastNameField.click();
	    String randomLastName = generateRandomString(5);
	    lastNameField.sendKeys(randomLastName);
		return randomLastName;
	}
	
	public void enterPhoneNumber() {
		WebElement phoneNumberField = driver.findElement(phoneNumber);
		phoneNumberField.click();
		String lastSevenDigits = generateRandomNumberString(7);
		String fullNumber = "456" + lastSevenDigits;
		phoneNumberField.sendKeys(fullNumber);
	}
	
	public void waitForToastMessage(int seconds) {
        waitForElementInvisibility(newGuestPopUp, seconds);
    }
	
	public String getGuestName() {
		return driver.findElement(guestNames).getText();
	}
	
	public List<String> getGuestList() {
	    List<WebElement> guestElements = driver.findElements(guestNames); 
	    List<String> guestNamesList = new ArrayList<>(); 

	    for (WebElement guestElement : guestElements) {
	        guestNamesList.add(guestElement.getText()); 
	    }
	    return guestNamesList;
	}
	
	public void tapChangeServer() {
		driver.findElement(changeServer).click();
	}
	
	public String getChangedServer() {
		return driver.findElement(currentServer).getText();
	}
		
	public void clickOnChangeServerBtn() {
		driver.findElement(changeServerBtn).click();
	}
	
	public String getSelectedServerName() {
		return driver.findElement(selectServerName).getText();
	}
	
	public List<String> getServerNameList(String currentServer) {
		List<WebElement> serverNames = driver.findElements(selectServerName);
		List<String> serverNamesList = new ArrayList<>(); 
		
		for(WebElement serverName : serverNames) {
			serverNamesList.add(serverName.getText());
		}
		
		if(serverNamesList.contains(currentServer)) {
			serverNamesList.remove(currentServer);
		}
		return serverNamesList;
	}
	
	public void assignServer(String server) {
		By serverLocator = By.xpath("//android.widget.TextView[@text='" + server + "']");
	    WebElement serverElement = driver.findElement(serverLocator);
	    serverElement.click();
	}
	
	public void tapOnPayByTender() {
		driver.findElement(payByTender).click();
	}
	
	public void tapOnServerWithTip() {
		driver.findElement(serverWithTip).click();
	}
	
	public boolean isAddATipScreen() {
		return driver.findElement(addATip).isDisplayed();
	}
	
	public void clickManualServiceCharge() {
		driver.findElement(manualServiceCharge).click();
	}
	
	public void tapOnManagerWithoutTip() {
		driver.findElement(managerWithoutTip).click();
	}
	
	public boolean isManagerApprovalPinScreen() {
		return driver.findElement(managerApproval).isDisplayed();
	}
	
	public void clickSearchButton() {
		driver.findElement(search).click();
	}
	
	public void clickSearchBar() {
		driver.findElement(searchBar).click();
	}
	
	public void thanksMessageText() {
		waitForElementVisibility(thanksMessage, 5);
	}
	
	public void searchItem(String itemName) {
		WebElement search = driver.findElement(searchBar);
		search.sendKeys(itemName);
	}
	
	public List<String> getSearchItemsList() {
		List<WebElement> searchedMenuItems = driver.findElements(searchedItems);
		List<String> searchedItemsList = new ArrayList<>();
		
		for(WebElement itemName : searchedMenuItems)
		{
			searchedItemsList.add(itemName.getText());
		}
		
		return searchedItemsList;
	}
	
	public void tapOnSpecialRequest() {
		driver.findElement(specialRequest).click();
	}
	
	public void addSpecialRequest(String specialReq) {
		 driver.findElement(addSpecialRequest).click();
		 driver.findElement(addSpecialRequest).sendKeys(specialReq);
	}
	
	public void tapOnDoneBtn() {
		driver.findElement(srDoneBtn).click();
	}
	
	public String getSpecialRequest() {
		return driver.findElement(specialReqText).getText();
	}
	
	public void scrollToTop() {
        // Construct the UiScrollable selector to scroll to end
        String uiScrollable = String.format(
            "new UiScrollable(new UiSelector().className(\"%s\")).scrollToBeginning(10)", "android.widget.ScrollView"
        );
 
        // Execute the UIAutomator command
        driver.findElement(AppiumBy.androidUIAutomator(uiScrollable));
    }
	public void unselectEmployeeDiscount() {
		driver.findElement(employeeDiscount).click();
	}

    public void tapPaymentTab() {
        driver.findElement(paymentTab).click();
    }

	public String getCustomerName() {
		return driver.findElements(customerName).get(1).getText();
	}

	public int getItemsCount() {
		return driver.findElements(totalItems).size();
	}

	public double getSubtotalOnPay() {
		return Double.parseDouble(driver.findElement(payBtn).getText().substring(5));
	}

	public void clickAroundTheWorld() {
		driver.findElement(aroundTheWorld).click();
	}

	public void clickApplyOnItem() {
		driver.findElement(applyOnItems).click();
	}

	public void itemToApplyDiscount() {
		driver.findElement(menuItemName).click();
	}

	public void clickOpenPercent() {
		driver.findElement(openPercent).click();
	}

	public void tickDiscountApply() {
		waitForElementVisibility(tickSplitBtn, 5).click();
	}

	public void clickBackBtn() {
		driver.findElement(backBtn).click();
	}

	public void addSaladAdOnModifier() {
		driver.findElement(By.xpath("//android.widget.TextView[contains(@resource-id, 'tvMenuItem') and @text='Sal Add ons']")).click();
	}

	public void increaseModifierQuantity() {
		List<WebElement> plusBtn = driver.findElements(By.xpath("//android.widget.ImageView[contains(@resource-id, 'ivAdd')]"));
		plusBtn.get(0).click();
	}

	public boolean isModifierAdded() {
		return driver.findElement(modifierText).isDisplayed();
	}

	public void clickCaesarSalad() {
		driver.findElement(saladItemName).click();
	}

	public void clickOpenDollar() {
		driver.findElement(openDollar).click();
	}
}
	
