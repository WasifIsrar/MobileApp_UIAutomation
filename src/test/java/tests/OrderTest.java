package tests;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import baseTest.POSBaseTest;
import pages.OrderPage;
import pages.PaymentPage;
import pages.PinPage;
import pages.SplitPage;

public class OrderTest extends POSBaseTest{
	
	@Test(groups= {"Smoke"})
	public void removeItemFromTicket() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		try {
			Thread.sleep(3000);
		}
		catch(Exception e) {
			
		}
		orderPage.editItem(0);
		Assert.assertTrue(orderPage.removeItem(), "Item not removed from ticket: ");
		Assert.assertTrue(orderPage.getTotalAmount() == 0, "Total Amount not zero: ");
	}

	@Test(groups = { "Smoke" })
	public void addItemAfter() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.waitForSubtotal();
		double subTotalAmount = orderPage.getSubTotalAmount();
		orderPage.stay();
		orderPage.addItem();
		Assert.assertTrue(orderPage.isItemAdded(2));
		double secondItemPrice = orderPage.getItemPrices().get(1);
		double subTotalAfter = roundHalfUp(subTotalAmount + secondItemPrice);
		Assert.assertEquals(orderPage.getSubTotalAmount(), subTotalAfter);
	}

	@Test(groups = { "Smoke" })
	public void cancelOneItemAfterSend() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.waitForSubtotal();
		orderPage.stay();
		orderPage.addSecondItem();
		orderPage.isItemAdded(2);
		double subTotalAmount = orderPage.getSubTotalAmount();
		double secondItemPrice = orderPage.getItemPrices().get(1);
		orderPage.clickCancelButton();
		double subTotalAfter = roundHalfUp(subTotalAmount - secondItemPrice);
		Assert.assertEquals(orderPage.getSubTotalAmount(), subTotalAfter);
	}

	@Test(groups = { "Smoke" })
	public void verifyAmounts() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.waitForSubtotal();
		double subTotal = orderPage.getSubTotalAmount();
		double expectedServiceCharges = roundHalfUp(subTotal * 0.25);
		Assert.assertEquals(orderPage.getServiceCharges(), expectedServiceCharges, "Wrong Service Charges: ");
		double serviceInclusive = Double.sum(subTotal, expectedServiceCharges);
		double serviceTax = calculateServiceTax(expectedServiceCharges);
		double subtotalTax = calculateTax(subTotal);
		double expectedTax = roundHalfUp(Double.sum(serviceTax, subtotalTax));
		double expectedTotal = roundHalfUp(Double.sum(serviceInclusive, expectedTax));
		Assert.assertEquals(orderPage.getTax(), expectedTax, "Wrong Tax Amount: ");
		Assert.assertEquals(orderPage.getTotalAmount(), expectedTotal, "Wrong Total Amount: ");
	}

	@Test(groups = { "Smoke" })
	public void splitTicketByGuests() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.waitForSubtotal();
		double totalAmount = orderPage.getSubTotalAmount();
		Assert.assertEquals(orderPage.getGuestsCount() - 2, 5, "Guests count incorrect on ticket header: ");
		splitPage = orderPage.splitTicket();
		splitPage.isScreenDisplayed();
		splitPage.splitByGuests();
		Assert.assertEquals(roundHalfUp(splitPage.getTotalCharges()), totalAmount,
				"Commulative amount of tickets incorrect on Split Screen: ");
		splitPage.done();
		orderPage.isPageDisplayed();
		Assert.assertEquals(orderPage.getTicketCount(), 5, "Ticket Count incorrect on order screen after Split: ");
	}

	@Test(groups = { "Smoke" })
	public void splitTicketByItems() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		splitPage = orderPage.splitTicket();
		splitPage.isScreenDisplayed();
		Assert.assertTrue(splitPage.verifyItemsSplit(), "Item not split on Split Screen: ");
		splitPage.done();
		orderPage.isPageDisplayed();
		Assert.assertEquals(orderPage.getItemsCount(), 2, "Item not split on Order Screen: ");
	}

	@Test(groups = { "Smoke" })
	public void splitItemtoTicket() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.waitForSubtotal();
		double subTotalBefore = orderPage.getSubTotalAmount();
		System.out.println("Sub total before: " + subTotalBefore);
		int ticketId = Integer.parseInt(orderPage.getTicketId());
		splitPage = orderPage.splitTicket();
		splitPage.isScreenDisplayed();
		Assert.assertTrue(splitPage.moveToTicket(), "Item not moved to new ticket: ");
		splitPage.done();
		orderPage.isPageDisplayed();
		double subTotalOnFirstTicket = orderPage.getSubTotalAmount();
		System.out.println("Sub total First: " + subTotalOnFirstTicket);
		ticketId = ticketId + 1;
		System.out.println(ticketId);
		orderPage.openNextTicket(1, Integer.toString(ticketId));
		double subTotalOnSecondTicket = orderPage.getSubTotalAmount();
		System.out.println("Sub total Second: " + subTotalOnSecondTicket);
		Assert.assertEquals(subTotalBefore, subTotalOnFirstTicket + subTotalOnSecondTicket, "Amount split incorrect: ");
	}
	@Test(groups = { "Smoke" })
	public void exemptTax() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.waitForSubtotal();
		double totalAmount = orderPage.getTotalAmount();
		double tax = orderPage.getTax();
		orderPage.openKebabMenu();
		orderPage.selectTaxExemption();
		orderPage.discountBtn();
		double totalAmountAfter = orderPage.getTotalAmount();
		Assert.assertEquals(totalAmountAfter, roundHalfUp(totalAmount - tax), "Total Amount after tax incorrect: ");
	}
	
	@Test(groups = { "Smoke" })
	public void percentageItemDiscount() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("1");
		orderPage.isItemAdded(1);
		double amountBefore=orderPage.getSubTotalAmount();
		System.out.println("subTotal Amount Before"+amountBefore);
		double calculatedDiscount= roundHalfUp(amountBefore*0.10);
		System.out.println("Calculated Discount Before"+calculatedDiscount);
		orderPage.editItem(0);
		orderPage.selectPercentComp();
		orderPage.enterCompDiscount();
		orderPage.doneBtn();
		double actualDiscount = Double.parseDouble(orderPage.getItemDisount());
		System.out.println("Calculated Discount After"+actualDiscount);
		Assert.assertEquals(calculatedDiscount, actualDiscount,"The Discount is not equal:");
	}
	@Test(groups = { "Smoke" })
	public void dollarItemDiscount() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("1");
		orderPage.isItemAdded(1);
		double subTotalAmount=orderPage.getSubTotalAmount();
		System.out.println("subTotal Amount Before"+subTotalAmount);
		orderPage.editItem(0);
		orderPage.selectDollarComp();
		orderPage.enterCompDiscount();
		orderPage.doneBtn();
		double appliedDiscount=subTotalAmount-10.00;
		System.out.println("applied Discount:"+appliedDiscount);
		double serviceCharge=orderPage.getServiceCharges();
		System.out.println("service Charge:"+serviceCharge);
		double serviceInclude=Double.sum(appliedDiscount,serviceCharge);
		System.out.println("service Include:"+serviceInclude);
		double totalAmountAfterDiscount=Double.sum(serviceInclude,orderPage.getTax());
		System.out.println("total Amount After Discount:"+totalAmountAfterDiscount);
		double actualDiscount = Double.parseDouble(orderPage.getItemDisount());
		System.out.println("Calculated Discount After"+actualDiscount);
		System.out.println("subtotal Amount:"+orderPage.getTotalAmount());
		Assert.assertEquals((orderPage.getTotalAmount()),totalAmountAfterDiscount,"Incorrect Total Amount after discount: ");
	}
	@Test(groups = { "Smoke" })
	public void applyDiscountAfterQuantityIncreases() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("1");
		orderPage.isItemAdded(1);
		double amountBefore=orderPage.getSubTotalAmount();
		System.out.println("subTotal Amount Before"+amountBefore);
		double calculatedDiscount= roundHalfUp(amountBefore*0.30);
		System.out.println("Calculated Discount Before"+calculatedDiscount);
		orderPage.editItem(0);
		orderPage.addMultiple(2);
		orderPage.doneBtn();
		orderPage.editItem(0);
		orderPage.selectPercentComp();
		orderPage.enterCompDiscount();
		orderPage.updateModifier();
		double actualDiscount = Double.parseDouble(orderPage.getItemDisount());
		System.out.println("Calculated Discount After"+actualDiscount);
		Assert.assertEquals(calculatedDiscount, actualDiscount,"The Discount is not equal:");
	}
	@Test(groups = { "Smoke" })
	public void itemDiscountWithPriceModifier() {
		enterPin();
		isTableScreenDisplayed();
		String table=tablePage.findEmptyTable();
		tablePage.openTable(table);
		orderPage=tablePage.addGuestsInTable("2");
		orderPage.isPageDisplayed();
		tableNum=orderPage.getTableNumber();
		ticketId=orderPage.getTicketId();
		orderPage.addItemWithModifier();
		orderPage.isItemAdded(1);
		orderPage.editItem(0);
		orderPage.addModifier();
		orderPage.doneBtn();
		orderPage.isModifierAdded();
		orderPage.editItem(0);
		orderPage.selectPercentComp();
		orderPage.enterCompDiscount();
		orderPage.updateModifier();
		double amountBefore=orderPage.getSubTotalAmount();
		System.out.println("subTotal Amount Before"+amountBefore);
		double calculatedDiscount= roundHalfUp(amountBefore*0.10);
		System.out.println("Calculated Discount Before"+calculatedDiscount);
		double actualDiscount = Double.parseDouble(orderPage.getItemDisount());
		System.out.println("Calculated Discount After"+actualDiscount);
		Assert.assertEquals(calculatedDiscount, actualDiscount,"The Discount is not equal:");
	}
	
	@Test(groups = { "Smoke" })
	public void applyDiscountBeforeQuantityIncreases() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("1");
		orderPage.isItemAdded(1);
		double amountBefore=orderPage.getSubTotalAmount();
		System.out.println("subTotal Amount Before"+amountBefore);
		double calculatedDiscount= roundHalfUp(amountBefore*0.30);
		System.out.println("Calculated Discount Before"+calculatedDiscount);
		orderPage.editItem(0);
		orderPage.selectPercentComp();
		orderPage.enterCompDiscount();
		orderPage.doneBtn();
		orderPage.editItem(0);
		orderPage.addMultiple(2);
		orderPage.doneBtn();
		double actualDiscount = Double.parseDouble(orderPage.getItemDisount());
		System.out.println("Calculated Discount After"+actualDiscount);
		Assert.assertEquals(calculatedDiscount, actualDiscount,"The Discount is not equal:");
	}

	@Test(groups = { "Smoke" })
	public void exemptTaxWithServer() {
		enterServerPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.waitForSubtotal();
		double totalAmount = orderPage.getTotalAmount();
		double tax = orderPage.getTax();
		orderPage.openKebabMenu();
		orderPage.selectTaxExemption();
		boolean isManagerApproval = orderPage.isManagerApprovalPinScreen();
		Assert.assertTrue(isManagerApproval, "Manager Approval Pin Screen is not displayed");
		orderPage.enterPOSManagerPin(managerPin.toCharArray());
	}

	@Test(groups = { "Smoke" })
	public void holdAndSend() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.verifyItemHeld();
		tablePage.openTable(tableNum);
		orderPage.isPageDisplayed();
		Assert.assertTrue(orderPage.sendToKDS());
		tablePage.openTable(tableNum);
	}
	
	@Test(groups = { "Smoke" })
	public void changeTable() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.waitForSubtotal();
		double totalAmountonFirstTable = orderPage.getTotalAmount();
		int itemsCountonFirstTable = orderPage.getItemsCount();
		orderPage.stay();
		orderPage.openKebabMenu();
		orderPage.clickChangeTable();
		tablePage.isTableVisible();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		tablePage.confirmTableMovement();
		orderPage.isPageDisplayed();
		Assert.assertEquals(orderPage.getTotalAmount(), totalAmountonFirstTable, "Total Amount does not match: ");
		Assert.assertEquals(orderPage.getItemsCount(), itemsCountonFirstTable, "Items Count does not match: ");
		Assert.assertEquals(orderPage.getTableNumber(), tableNum);
	}

	@Test(groups = { "Smoke" })
	public void changeTableWithServer() {
		enterServerPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.waitForSubtotal();
		double totalAmountonFirstTable = orderPage.getTotalAmount();
		int itemsCountonFirstTable = orderPage.getItemsCount();
		orderPage.stay();
		orderPage.openKebabMenu();
		orderPage.clickChangeTable();
		boolean isManagerApproval = orderPage.isManagerApprovalPinScreen();
		Assert.assertTrue(isManagerApproval, "Manager Approval Pin Screen is not displayed");
		orderPage.enterPOSManagerPin(managerPin.toCharArray());
	}

	@Test(groups = { "Smoke" })
	public void changeToSameTable() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.stay();
		orderPage.openKebabMenu();
		orderPage.clickChangeTable();
		tablePage.isTableVisible();
		tablePage.openTable(tableNum);
		tablePage.confirmTableMovement();
		Assert.assertTrue(tablePage.isTableVisible(), "Table Chnaged to Old Table");
		tablePage.openTable(tableNum);
	}

	@Test(groups = { "Smoke" })
	public void addItemQuantity() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.stay();
		double subTotalBefore = orderPage.getSubTotalAmount();
		orderPage.addItem();
		orderPage.isItemAdded(2);
		double secondItemTotal = orderPage.getItemPrices().get(1);
		double expectedSubtotal = Double.sum(subTotalBefore, secondItemTotal);
		Assert.assertEquals(orderPage.getSubTotalAmount(), roundHalfUp(expectedSubtotal));
	}

	@Test(groups = { "Smoke" })
	public void enableLargePartyAfterSplit() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("3");
		orderPage.isItemAdded(1);
		int ticketId = Integer.parseInt(orderPage.getTicketId());
		orderPage.clickGuestChip(2);
		orderPage.addSecondItem();
		orderPage.isItemAdded(1);
		orderPage.clickGuestChip(3);
		orderPage.addThirdItem();
		orderPage.isItemAdded(1);
		orderPage.clickGuestChip(4);
		orderPage.addFourthItem();
		orderPage.isItemAdded(1);
		splitPage = orderPage.splitTicket();
		splitPage.isScreenDisplayed();
		splitPage.splitByGuests();
		splitPage.done();
		orderPage.isPageDisplayed();
		orderPage.openServiceCharge();
		orderPage.uncheckAutoGratuity();
		orderPage.done();
		double expectedServiceCharges = roundHalfUp(orderPage.getSubTotalAmount() * 0.25);
		Assert.assertEquals(orderPage.getServiceCharges(), expectedServiceCharges,
				"Service Charges Incorrect After Unchecking: ");
		ticketId = ticketId + 1;
		orderPage.openNextTicket(1, Integer.toString(ticketId));
		double expectedSecondServiceCharges = roundHalfUp(orderPage.getSubTotalAmount() * 0.05);
		Assert.assertEquals(orderPage.getServiceCharges(), expectedSecondServiceCharges,
				"Service Charges Incorrect on Second Ticket: ");
		ticketId = ticketId + 1;
		orderPage.openNextTicket(2, Integer.toString(ticketId));
		double expectedThirdServiceCharges = roundHalfUp(orderPage.getSubTotalAmount() * 0.05);
		Assert.assertEquals(orderPage.getServiceCharges(), expectedThirdServiceCharges,
				"Service Charges Incorrect on Third Ticket: ");
	}

	@Test(groups = { "Smoke" })
	public void addQuantityModifier() {
		enterPin();
		isTableScreenDisplayed();
		String table = tablePage.findEmptyTable();
		tablePage.openTable(table);
		orderPage = tablePage.addGuestsInTable("5");
		orderPage.isPageDisplayed();
		tableNum = orderPage.getTableNumber();
		ticketId = orderPage.getTicketId();
		orderPage.addItemWithModifier();
		try {
			Thread.sleep(3000);
		}
		catch(Exception e) {
			
		}
		orderPage.waitForSubtotal();
		double itemPrice = orderPage.getFirstItemPrice();
		System.out.println("item price"+itemPrice);
		orderPage.editItem(0);
		orderPage.addModifier();
		orderPage.doneBtn();
		double modifierPrice = orderPage.getQunatityModifierPrice();
		System.out.println("modifier price"+modifierPrice);
		orderPage.editItem(0);
		orderPage.increaseModifierQuantity();
		orderPage.updateModifier();
		orderPage.waitForModifierAdded();
		double expectedModifierPrice = roundHalfUp(modifierPrice * 2);
		System.out.println("Expected modifier price"+expectedModifierPrice);
		Assert.assertEquals(orderPage.getModifierQuantity(), '2', "Modifier Quantity Incorrect: ");
		Assert.assertEquals(orderPage.getModifierPrice(), expectedModifierPrice, "Modifier Price Incorrect: ");
		Assert.assertEquals(orderPage.getSubTotalAmount(), roundHalfUp(Double.sum(itemPrice, expectedModifierPrice)),
				"Subtotal Incorrect: ");
	}

	
	
	@Test(groups = { "Smoke" })
	public void cancelOneItem() {
		enterPin();
		isTableScreenDisplayed();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("5");
		orderPage.isPageDisplayed();
		orderPage.addItem();
		orderPage.isItemAdded(1);
		try {
			Thread.sleep(3000);
		}
		catch(Exception e) {
			
		}
		orderPage.clickCancel();
		Assert.assertTrue(orderPage.waitForItemsCount(0), "Item(s) not removed");
		Assert.assertTrue(orderPage.getTotalAmount() == 0, "Total Amount not zero: ");

	}

	@Test(groups = { "Smoke" })
	public void voidItemAfterSplit() {
		enterPin();
		isTableScreenDisplayed();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("2");
		orderPage.isPageDisplayed();
		orderPage.addItem();
		splitPage = orderPage.splitTicket();
		splitPage.splitByGuests();
		splitPage.done();
		orderPage.isPageDisplayed();
		orderPage.waitForStay();
		orderPage.voidItem();
		Assert.assertTrue(orderPage.waitForSubtotal("0.00"), "Subtotal Not Equal to 0");
		int ticketNumber = Integer.parseInt(orderPage.getTicketId());
		orderPage.openNextTicket(1, String.valueOf(ticketNumber + 1));
		Assert.assertTrue(orderPage.waitForSubtotal("0.00"), "Subtotal Not Equal to 0");
	}

	@Test(groups = { "Smoke" })
	public void verifyPaymentsQSR() {
		enterPin();
		isTableScreenDisplayed();
		tablePage.clickHamburger();
		orderPage = tablePage.clickQSR();
		orderPage.addItem();
		orderPage.waitForServiceCharge();
		double serviceCharge = orderPage.getServiceCharges();
		double subTotal = orderPage.getSubTotalAmount();
		double tax = orderPage.getTax();
		double totalAmount = orderPage.getTotalAmount();
		orderPage.pay();
		Assert.assertEquals(orderPage.getServiceChargesPayment(), serviceCharge, "Service Charges Incorrect on ordershub: ");
		Assert.assertEquals(orderPage.getSubTotalPayment(), subTotal, "Subtotal Amount Incorrect on ordershub: ");
		Assert.assertEquals(orderPage.getTaxPayment(), tax, "Tax Incorrect on ordershub: ");
		Assert.assertEquals(orderPage.getTotalPayment(), totalAmount, "Total Amount Incorrect on ordershub: ");
		paymentPage = orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();

	}
	
	@Test(groups = { "Smoke" })
	public void AddModifierinQSR() {
		enterPin();
		isTableScreenDisplayed();
		tablePage.clickHamburger();
		orderPage = tablePage.clickQSR();
		orderPage.addItemWithModifier();
		orderPage.editItem(0);
		orderPage.editModifierinQSR();
		Assert.assertTrue(orderPage.waitForModifierAdded(), "The modifier is not added");
	}
	
	@Test(groups = { "Smoke" })
	public void addCustomItemInQSR() {
		enterPin();
		isTableScreenDisplayed();
		tablePage.clickHamburger();
		orderPage = tablePage.clickQSR();
		orderPage.isPageDisplayed();
		orderPage.openKebabMenu();
		orderPage.addCustomItem("Pizza");
		Assert.assertTrue(orderPage.isModifierAdded(),"The custom item is not displayed");
	}

	@Test(groups = { "Smoke" })
	public void LoginLogout() {
		enterPin();
		isTableScreenDisplayed();
		pinPage=tablePage.lockScreen();
		Assert.assertTrue(pinPage.isPinScreenDisplayed(), "Pin screen is displayed");

	}

	@Test(groups = { "Smoke" })
	public void addCustomer() {
		enterPin();
		isTableScreenDisplayed();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("2");
		orderPage.isPageDisplayed();
		orderPage.clickAddCustomer();
		orderPage.clickNewCustomer();
		String firstName = orderPage.enterFirstName();
		String lastName=orderPage.enterLastName();
		orderPage.enterPhone("2");
		orderPage.clickSave();
		Assert.assertTrue(orderPage.confirmName(), "Name is displayed");
		orderPage.clickCustomer();
		orderPage.enterSearchTerm(firstName+" "+lastName);
		Assert.assertTrue(orderPage.getCustomers().contains(firstName+" "+lastName),"Customer Name not Found: ");
	}

	@Test(groups = { "Smoke" })
	public void changeServer() {
		enterPin();
		isTableScreenDisplayed();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("2");
		orderPage.isPageDisplayed();
		String serverName= orderPage.getServerName();
		orderPage.openKebabMenu();
		orderPage.changeServe();
		String changedServer=orderPage.changeServerName(serverName);
		String[] splitStr = changedServer.split("\\s+");
		orderPage.change();
		String actualServer=orderPage.getServerName();
		Assert.assertEquals(actualServer, splitStr[0], "Server Names are different");

	}
	
	@Test(groups = { "Smoke" })
	public void changeServerWithServer() {
		enterServerPin();
		isTableScreenDisplayed();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("2");
		orderPage.isPageDisplayed();
		String serverName= orderPage.getServerName();
		orderPage.openKebabMenu();
		orderPage.changeServe();
		boolean isManagerApproval = orderPage.isManagerApprovalPinScreen();
		Assert.assertTrue(isManagerApproval, "Manager Approval Pin Screen is not displayed");
		orderPage.enterPOSManagerPin(managerPin.toCharArray());

	}
	@Test(groups = { "Smoke" })
	public void Takeout() {
		enterPin();
		isTableScreenDisplayed();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("5");
		orderPage.isPageDisplayed();
		orderPage.clickTakeoutBtn();
		orderPage.addItem();
		orderPage.waitForSubtotal();
		double subTotal = orderPage.getSubTotalAmount();
		System.out.println(subTotal);
		double ServiceCharge =roundHalfUp(subTotal*0.25);
		System.out.println(ServiceCharge);
		Assert.assertEquals(orderPage.getServiceCharges(), ServiceCharge, "The service charges are wrong ");
		double subTotalTax = calculateTakeoutTax(subTotal);
		System.out.println(subTotalTax);
		double ServiceChargeTax= calculateTakeoutServiceTax(ServiceCharge);
		System.out.println(ServiceChargeTax);
		double expectedTax = roundHalfUp(Double.sum(ServiceChargeTax, subTotalTax));
		System.out.println(expectedTax);
		double tax = orderPage.getTax();
		System.out.println(tax);
		Assert.assertEquals(tax, expectedTax, "The tax and total tax are not equal");
	}
	
	@Test(groups = { "Smoke" })
	public void applyServiceChargesManually() {
		enterPin();
		isTableScreenDisplayed();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("5");
		orderPage.isPageDisplayed();
		orderPage.addItem();
		try {
			Thread.sleep(3000);
		}
		catch(Exception e) {
			
		}
		double subTotal = orderPage.getSubTotalAmount();
		System.out.println(subTotal);
		double expectedServiceCharge =roundHalfUp(subTotal*0.35);
		System.out.println(expectedServiceCharge);
		orderPage.clickServiceCharges();
		orderPage.clickManualServiceCharge();
		orderPage.clickDone();
		double actualServiceCharges = orderPage.getServiceCharges();
		System.out.println(actualServiceCharges);
		Assert.assertEquals(actualServiceCharges, expectedServiceCharge, "The service charge are not equal");
	}
	
	@Test(groups = { "Smoke" })
	public void applyServiceChargesManuallyWithServer() {
		enterServerPin();
		isTableScreenDisplayed();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("5");
		orderPage.isPageDisplayed();
		orderPage.addItem();
		double subTotal = orderPage.getSubTotalAmount();
		System.out.println(subTotal);
		double expectedServiceCharge =roundHalfUp(subTotal*0.35);
		System.out.println(expectedServiceCharge);
		orderPage.clickServiceCharges();
		boolean isManagerApproval = orderPage.isManagerApprovalPinScreen();
		Assert.assertTrue(isManagerApproval, "Manager Approval Pin Screen is not displayed");
		orderPage.enterPOSManagerPin(managerPin.toCharArray());
	}
	@Test(groups = { "Smoke" })
	public void searchMenu() {
		enterPin();
		isTableScreenDisplayed();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("5");
		orderPage.isPageDisplayed();
		String searchedTerm = orderPage.enterSearchTerm("Slider");
		boolean isMatchFound = orderPage.verifySearchResultInList(searchedTerm);
		Assert.assertTrue(isMatchFound, "The search term '" + searchedTerm + "' was not found in the list of results.");
	}
	@Test(groups = { "Regression" })
	public void removeItemWithCancelButton() {
		enterPin();
		isTableScreenDisplayed();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("5");
		orderPage.isPageDisplayed();
		orderPage.addItem();
		orderPage.addSecondItem();
		orderPage.addThirdItem();
		orderPage.addFourthItem();
		orderPage.isItemAdded(4);
		orderPage.clickCancelButton();
		int expectedItem=orderPage.getItemCount();
		Assert.assertEquals(expectedItem, 0, "The item is not removed");
	}
	@Test(groups = { "Regression" })
	public void removeItemWithCancelButtonAfterSent() {
		enterPin();
		isTableScreenDisplayed();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("5");
		orderPage.isPageDisplayed();
		orderPage.addItem();
		orderPage.addSecondItem();
		orderPage.addThirdItem();
		orderPage.isItemAdded(3);
		orderPage.stay();
		orderPage.addFourthItem();
		orderPage.clickCancelButton();
		int expectedItem=orderPage.getItemCount();
		Assert.assertEquals(expectedItem, 3, "The item is not removed after sent ");
	}
	@Test(groups = { "Regression" })
	public void increaseGuestItemsDisappear() {
		enterPin();
		isTableScreenDisplayed();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("2");
		orderPage.isPageDisplayed();
		orderPage.addItem();
		orderPage.addSecondItem();
		orderPage.addThirdItem();
		orderPage.addFourthItem();
		orderPage.isItemAdded(4);
		orderPage.editGuests();
		orderPage.increaseGuests("5");
		int expectedItem=orderPage.getItemCount();
		Assert.assertEquals(expectedItem, 4, "The items are not same when increased guests:");
		
	}
	
}