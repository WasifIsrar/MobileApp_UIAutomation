package tests;




import org.testng.Assert;
import org.testng.annotations.Test;

import baseTest.POSBaseTest;
import pages.PaymentPage;


public class PaymentTest extends POSBaseTest{

	@Test(groups= {"Smoke"})
	public void applyDiscountAfterSplit(){
		enterPin_addGuestsAndItem("2");
		splitPage= orderPage.splitTicket();
		splitPage.splitByGuests();
		splitPage.done();
		orderPage.waitForSubtotal();
		double totalAmount= orderPage.getTotalAmount();
		orderPage.openKebabMenu();
		orderPage.addPercentCheckDiscount();
		orderPage.waitForDisount();
		double totatAfterSplit = roundHalfUp( totalAmount*0.1);
		double splitAmount = totalAmount - totatAfterSplit;
		double subTotalAmount=orderPage.getSubTotalAmount();
		String discount=String.format("%.2f", subTotalAmount*0.1);
		
		double appliedDiscount=roundHalfUp(subTotalAmount-Double.parseDouble(discount));
		System.out.println("SubTotalAfter: "+appliedDiscount);
		double serviceCharge=orderPage.getServiceCharges();
		double serviceInclude=Double.sum(appliedDiscount,serviceCharge);
		System.out.println("SubTotalWithService: "+serviceInclude);
		double totalAmountAfterDiscount=roundHalfUp(Double.sum(serviceInclude,orderPage.getTax()));
		Assert.assertEquals(orderPage.getTotalAmount(),totalAmountAfterDiscount,"Incorrect Total Amount after discount: ");
		orderPage.sendAndProceed();
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		
	}
	
	@Test(groups= {"Smoke"})
	public void payByCash() {
		enterPin_addGuestsAndItem("5");
		Assert.assertTrue(orderPage.isItemAdded(1),"Item not added to ticket: ");
		Assert.assertTrue(orderPage.sendToKDS(),"Table Screen not displayed after ticket sent: ");
		tablePage.openTable(tableNum);
		Assert.assertTrue(orderPage.isPageDisplayed(),"Order Screen not displayed on opening occupied table: ");
		orderPage.pay();
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		Assert.assertTrue(tablePage.isTableVisible(),"Table Screen not displayed after Payment: ");
	}	
	
	@Test(groups= {"Smoke"})
	public void openPercentageCheckDiscount() {
		enterPin_addGuestsAndItem("5");
		orderPage.waitForSubtotal();
		double subTotalAmount=orderPage.getSubTotalAmount();
		orderPage.openKebabMenu();
		orderPage.addPercentCheckDiscount();
		orderPage.waitForDisount();
		String discount=String.format("%.2f", subTotalAmount*0.1);
		System.out.println("Discount: "+discount);
		double appliedDiscount=roundHalfUp(subTotalAmount-Double.parseDouble(discount));
		System.out.println("SubTotalAfter: "+appliedDiscount);
		double serviceCharge=orderPage.getServiceCharges();
		double serviceInclude=Double.sum(appliedDiscount,serviceCharge);
		System.out.println("SubTotalWithService: "+serviceInclude);
		double totalAmountAfterDiscount=roundHalfUp(Double.sum(serviceInclude,orderPage.getTax()));
		Assert.assertEquals(orderPage.getTotalAmount(),totalAmountAfterDiscount,"Incorrect Total Amount after discount: ");
		orderPage.sendAndProceed();
		Assert.assertEquals(orderPage.getTotal(), orderPage.getBalanceDuePayment(),"Balance Due and total unequal: ");
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
	}
	
	@Test(groups= {"Smoke"})
	public void openPercentageCheckDiscountWithServer() {
		enterServerPin();
		isTableScreenDisplayed();
		addGuestsAndItem("2");
		orderPage.waitForSubtotal();
		orderPage.openKebabMenu();
		orderPage.discountBtn();
		boolean isManagerApproval = orderPage.isManagerApprovalPinScreen();
		Assert.assertTrue(isManagerApproval, "Manager Approval Pin Screen is not displayed");
		orderPage.enterPOSManagerPin(managerPin.toCharArray());
	}
	@Test(groups= {"Smoke"})
	public void openDollarsCheckDiscount() {
		enterPin_addGuestsAndItem("5");
		orderPage.waitForSubtotal();
		double subTotalAmount=orderPage.getSubTotalAmount();
		orderPage.openKebabMenu();
		orderPage.addDollarsCheckDiscount();
		orderPage.waitForDisount();
		double appliedDiscount=subTotalAmount-10.00;
		double serviceCharge=orderPage.getServiceCharges();
		double serviceInclude=Double.sum(appliedDiscount,serviceCharge);
		double totalAmountAfterDiscount=Double.sum(serviceInclude,orderPage.getTax());
		Assert.assertEquals((orderPage.getTotalAmount()),totalAmountAfterDiscount,"Incorrect Total Amount after discount: ");
		orderPage.sendAndProceed();
		Assert.assertEquals(orderPage.getTotal(), orderPage.getBalanceDuePayment(),"Balance Due and total unequal: ");
		paymentPage=orderPage.getPaymentPage();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		
	}
	
	@Test(groups= {"Smoke"})
	public void managerCheckDiscount() {
		enterPin_addGuestsAndItem("5");
		orderPage.waitForSubtotal();
		orderPage.openKebabMenu();
		orderPage.addManagerCheckDiscount();
		orderPage.waitForDisount();
		Assert.assertEquals(orderPage.getTotalAmount(),0,"Incorrect Total Amount after discount: ");
		orderPage.sendAndProceed();
		Assert.assertEquals(orderPage.getTotal(), orderPage.getBalanceDuePayment(),"Balance Due and total unequal: ");
		orderPage.goBackToMenu();
		orderPage.isPageDisplayed();
	}
	
	@Test(groups= {"Smoke"})
	public void verifyAmountAddedinDrawer() {
		enterPin();
		isTableScreenDisplayed();
		cashDrawerPage=tablePage.gotoCashDrawer();
		int reopenBtnStation=cashDrawerPage.openDrawer("This Station");
		if(reopenBtnStation>0) {
			cashDrawerPage.reopenDrawer();
		}
		double totalBalanceBefore=cashDrawerPage.getTotalBalance();
		cashDrawerPage.goBacktoTableServe();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.sendAndProceed();
		PaymentPage paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		double cashRendered=Double.parseDouble(paymentPage.getCashTendered());
		paymentPage.completePayment();
		paymentPage.noReceipt();
		tablePage.isTableVisible();
		tablePage.gotoCashDrawer();
		cashDrawerPage.openDrawer("This Station");
		double totalBalanceAfter=cashDrawerPage.getTotalBalance();
		String calculatedAmount=String.format("%.2f", totalBalanceBefore+cashRendered);
		Assert.assertEquals(totalBalanceAfter,Double.parseDouble(calculatedAmount),"Amount in drawer incorrect after a payment: ");
	}
	
	@Test(groups= {"Smoke"})
	public void partialPay() {
		enterPin_addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.sendAndProceed();
		orderPage.waitForOrderScreenToDisappear();
		double balanceDue=orderPage.getBalanceDuePayment();
		orderPage.splitPaymentAmount();
		double paymentAmount=orderPage.getPaymentAmount();
		double calculated=roundHalfUp(balanceDue/2);
		Assert.assertEquals(paymentAmount,calculated,"Payment Amount incorrect after payment split: ");
		double calculatedAmount=roundHalfUp(balanceDue-paymentAmount);
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		Assert.assertEquals(orderPage.getBillStatus(), "Partial","Partial Payment Status incorrect after partial payment: ");
		System.out.println(orderPage.getBalanceDuePayment());
		Assert.assertEquals(orderPage.getBalanceDuePayment(),calculatedAmount,"Balance Due Incorrect after partial payment: ");
		ordershubPage=orderPage.gotoOrdershub();
		Assert.assertEquals(ordershubPage.getStatus(ticketId),"partial","Ticket status incorret in Ordershub: ");
	}
	
	@Test(groups= {"Smoke"})
	public void splitAndPay() {
		enterPin_addGuestsAndItem("2");
		orderPage.isItemAdded(1);
		splitPage=orderPage.splitTicket();
		splitPage.isScreenDisplayed();
		splitPage.splitByGuests();
		splitPage.done();
		orderPage.isPageDisplayed();
		double serviceCharges=orderPage.getServiceCharges();
		double subTotalAmount=orderPage.getSubTotalAmount();
		double tax=orderPage.getTax();
		double totalAmount=orderPage.getTotalAmount();
		orderPage.sendAndProceed();
		orderPage.waitForPaymentTicket();
		Assert.assertEquals(orderPage.getServiceChargesPayment(),serviceCharges, "Service Charges are not equal");
		Assert.assertEquals(orderPage.getSubTotalPayment(),subTotalAmount, "Sub Total Amount is not equal");
		Assert.assertEquals(orderPage.getTaxPayment(),tax, "Tax is not equal");
		Assert.assertEquals(orderPage.getTotalPayment(),totalAmount, "Total Amount not equal");
		paymentPage = orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		double serviceCharges1=orderPage.getServiceCharges();
		double subTotalAmount1=orderPage.getSubTotalAmount();
		double tax1=orderPage.getTax();
		double totalAmount1=orderPage.getTotalAmount();
		orderPage.pay();
		Assert.assertEquals(orderPage.getServiceChargesPayment(),serviceCharges1, "Service Charges are not equal");
		Assert.assertEquals(orderPage.getSubTotalPayment(),subTotalAmount1, "Sub Total Amount is not equal");
		Assert.assertEquals(orderPage.getTaxPayment(),tax1, "Tax is not equal");
		Assert.assertEquals(orderPage.getTotalPayment(),totalAmount1, "Total Amount not equal");
		paymentPage = orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
	}
	
	
	@Test(groups= {"Smoke"})
	public void customPartialPayment() {
		enterPin_addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.sendAndProceed();
		orderPage.waitForOrderScreenToDisappear();
		orderPage.splitPaymentAmount();
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		double paymentAmountBefore=orderPage.getPaymentAmount();
		orderPage.editPaymentAmount();
		double paymentAmountAfter=orderPage.getPaymentAmount();
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		Assert.assertEquals(orderPage.getPaymentAmount(), roundHalfUp(paymentAmountBefore-paymentAmountAfter));
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
	}
	
	@Test(groups= {"Smoke"})
	public void uncheckServiceCharge() {
		enterPin_addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		try {
			Thread.sleep(3000);
		}
		catch(Exception e) {
			
		}
		orderPage.openServiceCharge();
		orderPage.uncheckAutoGratuity();
		orderPage.done();
		double expectedServiceCharges=roundHalfUp(orderPage.getSubTotalAmount()*0.05);
		Assert.assertEquals(orderPage.getServiceCharges(), expectedServiceCharges,"Service Charges Incorrect After Unchecking: ");
		double serviceInclusive=Double.sum(orderPage.getSubTotalAmount(), expectedServiceCharges);
		double serviceTax=calculateServiceTax(expectedServiceCharges);
		double subtotalTax=calculateTax(orderPage.getSubTotalAmount());
		double expectedTax=roundHalfUp(Double.sum(serviceTax, subtotalTax));
		double expectedTotal=roundHalfUp(Double.sum(serviceInclusive, expectedTax));
		Assert.assertEquals(orderPage.getTax(),expectedTax,"Wrong Tax Amount: ");
		Assert.assertEquals(orderPage.getTotalAmount(), expectedTotal,"Wrong Total Amount: ");
		orderPage.sendAndProceed();
		orderPage.waitForPaymentTicket();
		Assert.assertEquals(orderPage.getServiceChargesPayment(),expectedServiceCharges,"Service Charges Unequal on Payment: ");
		Assert.assertEquals(orderPage.getTaxPayment(),expectedTax,"Tax Unequal on Payment: ");
		Assert.assertEquals(orderPage.getBalanceDuePayment(),expectedTotal,"Balance Due Unequal on Payment: ");
		Assert.assertEquals(orderPage.getTotalPayment(),expectedTotal,"Total Unequal on Payment: ");
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
	}
	
	@Test(groups= {"Smoke"})
	public void uncheckAllServiceCharges() {
		enterPin_addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		try {
			Thread.sleep(3000);
		}
		catch(Exception e) {
			
		}
		orderPage.openServiceCharge();
		orderPage.uncheckAllServiceCharges();
		orderPage.done();
		orderPage.waitForServiceCharge("0.00");
		Assert.assertEquals(orderPage.getServiceCharges(), 0,"Service Charges Incorrect After Unchecking: ");
		double subtotalTax=calculateTax(orderPage.getSubTotalAmount());
		double expectedTotal=roundHalfUp(Double.sum(orderPage.getSubTotalAmount(), subtotalTax));
		Assert.assertEquals(orderPage.getTax(),subtotalTax,"Wrong Tax Amount: ");
		Assert.assertEquals(orderPage.getTotalAmount(), expectedTotal,"Wrong Total Amount: ");
		orderPage.sendAndProceed();
		orderPage.waitForPaymentTicket();
		Assert.assertEquals(orderPage.getServiceChargesPayment(),0,"Service Charges Unequal on Payment: ");
		Assert.assertEquals(orderPage.getTaxPayment(),subtotalTax,"Tax Unequal on Payment: ");
		Assert.assertEquals(orderPage.getBalanceDuePayment(),expectedTotal,"Balance Due Unequal on Payment: ");
		Assert.assertEquals(orderPage.getTotalPayment(),expectedTotal,"Total Unequal on Payment: ");
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
	}
	
	
	@Test(groups = {"Smoke"})
	public void payWithCustomItem() {
		enterPin_addGuestsAndItem("2");
		orderPage.waitForSubtotal();
		double subtotalBefore=orderPage.getSubTotalAmount();
		System.out.println("SUBTOTAL BEFORE: "+subtotalBefore);
		orderPage.clickGuestChip(2);
		orderPage.openKebabMenu();
		orderPage.addCustomItem("Tequilla");
		double subtotalToBe=roundHalfUp(Double.sum(subtotalBefore, 10));
		System.out.println("SUBTOTAL To Be: "+subtotalToBe);
		System.out.println("STRING SUBTOTAL To Be: "+Double.toString(subtotalToBe));
		orderPage.waitForSubtotal(subtotalToBe);
		double serviceCharges=orderPage.getServiceCharges();
		double subTotalAmount=orderPage.getSubTotalAmount();
		double tax=orderPage.getTax();
		double totalAmount=orderPage.getTotalAmount();
		orderPage.sendAndProceed();
		orderPage.waitForPaymentTicket();
		Assert.assertEquals(orderPage.getServiceChargesPayment(),serviceCharges, "Service Charges are not equal");
		Assert.assertEquals(orderPage.getSubTotalPayment(),subTotalAmount, "Sub Total Amount is not equal");
		Assert.assertEquals(orderPage.getTaxPayment(),tax, "Tax is not equal");
		Assert.assertEquals(orderPage.getTotalPayment(),totalAmount, "Total Amount not equal");	
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
	}
	
	private void payAndOpenRefund() {
		orderPage.sendAndProceed();
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		tablePage.isTableVisible();
		ordershubPage=tablePage.gotoOrdershub();
		ordershubPage.openEntry(ticketId);
		ordershubPage.gotoPayment();
		ordershubPage.gotoRefund();
	}
	
	private void enterPin_addGuestsAndItem(String guests) {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem(guests);
	}
	
	
}
