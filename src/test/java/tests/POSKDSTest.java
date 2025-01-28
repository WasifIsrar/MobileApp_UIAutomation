package tests;


import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import baseTest.POSKDSBaseTest;
import pages.OrdershubPage;
import pages.PaymentPage;


public class POSKDSTest extends POSKDSBaseTest{

	

	@Test(groups= {"Smoke"})
	public void payAndFullFillOrder() {
        enterPinSelectStationAddGuestsItems();
        String ticketID = orderPage.getTicketId();
        System.out.println(ticketID);
        orderPage.sendAndProceed();
        orderPage.waitForPaymentTicket();
        PaymentPage paymentpage=orderPage.payByCash();
		paymentpage.selectAmount();
		paymentpage.completePayment();
		paymentpage.noReceipt();
		kds.getTicketNumber();
		kds.openTicket();
		kds.selectAll();
		kds.fulfill();
		kds.isDisappear();
		OrdershubPage ordershub= tablePage.gotoOrdershub();
	    ordershub.gotoCompleted();
	    System.out.println(ordershub.getTicketID());
	    Assert.assertTrue(ordershub.isTicketDisplayed(ticketID));
	}

	@Test(groups= {"Smoke"})
	public void stayOnTicket() {
		enterPinSelectStationAddGuestsItems();
		String serverName=orderPage.getServerName();
		Assert.assertTrue(orderPage.stay(),"Stay status not displayed: ");
		Assert.assertEquals(kds.getTicketNumber(),ticketId,"Ticket not found  on KDS: ");
		Assert.assertEquals(kds.getServerName(),serverName,"Server Name incorrect on KDS: ");
	}
	
	
	@Test(groups= {"Smoke"})
	public void holdOneItem() {
		enterPinSelectStationAddGuestsItems();
		orderPage.addSecondItem();
		orderPage.isItemAdded(2);
		orderPage.editItem(0);
		Assert.assertTrue(orderPage.verifyItemHeld(),"Table Screen not displayed on holding item: ");
		tablePage.openTable(tableNum);
		Assert.assertEquals(kds.getTicketNumber(),ticketId,"Ticket not found  on KDS: ");
	}
	
	@Test(groups= {"Smoke"})
	public void updateQuantityAfterSent() {
		enterPinSelectStationAddGuestsItems();
		String itemName=orderPage.getItemNames().get(0);
		int quantity=orderPage.getQuantity();
		String text=quantity+" "+itemName;
		System.out.println("Text "+text);
		orderPage.sendToKDS();
		kds.getTicketNumber();
		tablePage.openTable(tableNum);
		Assert.assertTrue(orderPage.addItemAfter(),"Quantity not updated on in ticket: ");
		Assert.assertEquals(kds.getQuantity(),'2',"Quantity not updated on KDS: ");
	}
	
	@Test(groups= {"Smoke"})
	public void editModifierAfterSentToKDS() {
		enterPin();
		tablePage.isTableVisible();
		kds.confirmStation();
		addGuestsAndGetTicketId();
		orderPage.addItemWithModifier();
		orderPage.addModifierTill();
		orderPage.stay();
		kds.getTicketNumber();
		orderPage.editItem(0);
		orderPage.confirmEdit();
		String expectedModifier=orderPage.editModifier();
		System.out.println("EXPECTED MODIFIER:"+expectedModifier);
		kds.openTicket();
		Assert.assertTrue(kds.getModifier().contains(expectedModifier),"Modifier incorrect on kds: ");
	}
	
	@Test(groups= {"Smoke"})
	public void unfulfillOrder() {
		enterPinSelectStationAddGuestsItems();
		orderPage.sendToKDS();
		tablePage.openTable(tableNum);
		kds.getTicketNumber();
		kds.openTicket();
		kds.selectAll();
		kds.fulfill();
		kds.isDisappear();
		kds.tapRecent();
		kds.openTicket();
		kds.selectAll();
		kds.fulfill();
		Assert.assertTrue(kds.isRecalled(),"Recalled Status Not Displayed");
	}
	
	@Test(groups= {"Smoke"})
	public void unfulfillItem() {
		enterPinSelectStationAddGuestsItems();
		orderPage.addItem();
		orderPage.isItemAdded(2);
		orderPage.sendToKDS();
		tablePage.openTable(tableNum);
		kds.getTicketNumber();
		kds.openTicket();
		kds.selectAll();
		kds.fulfill();
		kds.isDisappear();
		kds.tapRecent();
		kds.openTicket();
		kds.selectItem();
		kds.fulfill();
		Assert.assertTrue(kds.isRecalled(),"Recalled Status Not Displayed");
	}
	
	@Test(groups= {"Smoke"},priority=-1)
	public void addNote() {
		enterPin();
		kds.closeDialog();
		kds.selectFirstStation();
		kds.confirmStation();
		addGuestsAndGetTicketId();
		orderPage.addItem();
		orderPage.isItemAdded(1);
		String note="Special";
		Assert.assertTrue(orderPage.verifyNoteDisplayed(note),"Note not displayed in ticket: ");
		orderPage.sendToKDS();
		tablePage.openTable(tableNum);
		kds.getTicketNumber();
		Assert.assertTrue(kds.getTicketDetails().contains(note),"Note incorrect on KDS: ");
	}
	
	@Test(groups= {"Smoke"})
	public void sendSplitTicket() {
		enterPinSelectStationAddGuestsItems();
		splitPage=orderPage.splitTicket();
		splitPage.splitByGuests();
		splitPage.done();
		orderPage.isPageDisplayed();
		orderPage.stay();
		Assert.assertEquals(kds.getTicketNumber(),ticketId,"Ticket not found  on KDS: ");
	}
	
	@Test(groups= {"Smoke"})
	public void voidOrder() {
		enterPinSelectStationAddGuestsItems();
		orderPage.stay();
		kds.getTicketNumber();
		orderPage.openKebabMenu();
		orderPage.voidTicket();
		orderPage.waitForTables();
		Assert.assertTrue(kds.isDisappear());
	}
	
	@Test(groups= {"Smoke"})
	public void voidItem() {
		enterPinSelectStationAddGuestsItems();
		orderPage.addSecondItem();
		orderPage.isItemAdded(2);
		orderPage.stay();
		kds.getTicketNumber();
		double subTotalBefore=orderPage.getSubTotalAmount();
		double itemPrice=orderPage.getItemPrices().get(0);
		orderPage.editItem(0);
		orderPage.confirmEdit();
		orderPage.voidSelectedItem();
		orderPage.waitForMenu();
		String firstItem=orderPage.getItemNames().get(0);
		String secondItem=orderPage.getItemNames().get(1);
		double expectedSubTotal=roundHalfUp(subTotalBefore-itemPrice);
		Assert.assertEquals(orderPage.getSubTotalAmount(), expectedSubTotal);
		kds.openTicket();
		kds.selectItem(firstItem);
		Assert.assertEquals(kds.getClickable(),"false");
		kds.selectItem(firstItem);
		kds.selectItem(secondItem);
		Assert.assertEquals(kds.getClickable(),"true");
	}
	
	
	@Test(groups = {"Smoke"})
	public void decreaseItemQuantity()
	{
		enterPinSelectStationAddGuestsItems();
		orderPage.increaseQuantityTill();
		orderPage.stayHere();
		kds.getTicketNumber();
		orderPage.editItem(0);
		orderPage.confirmEdit();
		orderPage.decreaseItemQuantity(1);
		orderPage.updateAfterSent();
		orderPage.waitForQuantity(1);
		kds.openTicket();
		Assert.assertEquals(kds.getQuantity(),'1',"Quantity not updated on KDS: ");
	}
	
	@Test(groups = {"Smoke"})
	public void fulfillUnpaidTicket()
	{
		enterPinSelectStationAddGuestsItems();
		String ticketId = orderPage.getTicketId();
		orderPage.sendToKDS();
		kds.getTicketNumber();
		kds.openTicket();
		kds.selectAll();
		kds.fulfill();
		OrdershubPage orderHub = orderPage.gotoOrdershub();
		orderHub.goToReadyOrders();
		Assert.assertEquals(Integer.parseInt(ticketId), Integer.parseInt(orderHub.getTicketID()), "Ticket ID is not equal");
	}
	
	@Test(groups = {"Smoke"})
	public void callServer()
	{
		enterPinSelectStationAddGuestsItems();
		orderPage.sendToKDS();
		kds.getTicketNumber();
		kds.openTicket();
		kds.tapCallServer();
		Assert.assertEquals(orderPage.getCallServerText().replaceAll("[\\s]+", " ").strip(), "Chef needs you in the kitchen!", "Not Equal");
		Assert.assertTrue(kds.isCallServerButtonDisabled(), "The Call Server button should be disabled");
	}
	
	private void enterPinSelectStationAddGuestsItems() {
		enterPin();
		tablePage.isTableVisible();
		kds.confirmStation();
		addGuestsAndGetTicketId();
		orderPage.addItem();
		orderPage.isItemAdded(1);
	}
	
	@Test(groups = {"Smoke"})
	public void addItemAfterSent()
	{
		enterPinSelectStationAddGuestsItems();
		orderPage.stay();
		orderPage.addItem();
		System.out.println(ticketId);
		orderPage.sendToKDS();
		int count= kds.getTwoTickets();
		System.out.println(count);
		Assert.assertEquals(count,2, "The ticket number does not matched");
	}
	@Test(groups= {"Regression"})
	public void customItemInQSR() {
        enterPin();
        tablePage.isTableVisible();
        kds.confirmStation();
		tablePage.clickHamburger();
		orderPage = tablePage.clickQSR();
		orderPage.isPageDisplayed();
		orderPage.openKebabMenu();
		orderPage.addCustomItem("Pizza");
		orderPage.isItemAdded(1);
        String ticketID = orderPage.getTicketId();
        kds.setTicketId(ticketID);
        System.out.println("POS ticket id:"+ticketID);
        orderPage.pay();
        orderPage.waitForPaymentTicket();
        PaymentPage paymentpage=orderPage.payByCash();
		paymentpage.selectAmount();
		paymentpage.completePayment();
		paymentpage.noReceipt();
		String kdsTicket=kds.getTicketNumber();
		System.out.println("KDS ticket id:"+kdsTicket);
		kds.openTicket();
		kds.selectAll();
		kds.fulfill();
		kds.isDisappear();
		OrdershubPage ordershub= tablePage.gotoOrdershub();
	    ordershub.gotoCompleted();
	    System.out.println("OrdersHub ticket id:"+ordershub.getTicketID());
	    Assert.assertTrue(ordershub.isTicketDisplayed(ticketID));
	}
	
}
