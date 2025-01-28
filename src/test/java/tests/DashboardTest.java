package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import baseTest.POSBaseTest;
import pages.DashboardPage;
import pages.PaymentPage;

public class DashboardTest extends POSBaseTest{

	@Test(priority=-2,groups= {"Smoke"})
	public void verifyClockInTime() {
		enterPin();
		tablePage.verifyClockIn();
		dashboardPage=tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		Assert.assertTrue(dashboardPage.getClockInTime(deviceTime),"Clockin Time not found: ");
	}
	
	@Test(priority=-1,groups= {"Smoke"})
	public void verifySalesAndTaxOnDashboard() {
		enterPin();
		tablePage.checkContinueHere();
		tablePage.isTableVisible();
		dashboardPage=tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		double salesAmountBefore=Double.parseDouble(dashboardPage.getSales());
		dashboardPage.openOverview();
		double actualSalesBefore=Double.parseDouble(dashboardPage.getActualSales());
		double totalSalesBefore=Double.parseDouble(dashboardPage.getTotalSales());
		int totalChecks=dashboardPage.getTotalChecks();
		int guestsServed=dashboardPage.getGuestsServed();
		dashboardPage.openZReport();
		double netSales=Double.parseDouble(dashboardPage.getNetSales());
		double taxAmount=Double.parseDouble(dashboardPage.getTaxes());
		double cashAmount=Double.parseDouble(dashboardPage.getCashAmount());
		double surcharges=Double.parseDouble(dashboardPage.getSurcharges());
		double gratuity=Double.parseDouble(dashboardPage.getGratuity());
		dashboardPage.gotoTableServe();
		addGuestsAndItem("5");
		orderPage.waitForSubtotal();
		double totalAmount=orderPage.getTotalAmount();
		System.out.println("Total Amount:"+totalAmount);
		double subTotal=orderPage.getSubTotalAmount();
		System.out.println("Subtotal Amount:"+subTotal);
		String expectedSalesAmount=String.format("%.2f",Double.sum(salesAmountBefore, totalAmount));
		String expectedActualSalesAmount=String.format("%.2f",Double.sum(actualSalesBefore, subTotal));
		String expectedTotalSalesAmount=String.format("%.2f",Double.sum(totalSalesBefore, subTotal));
		double tax=orderPage.getTax();
		String expectedTax=String.format("%.2f",Double.sum(taxAmount, tax));
		String expectedNetSales=String.format("%.2f",Double.sum(netSales,subTotal));
		String expectedCashAmount=String.format("%.2f", Double.sum(cashAmount, totalAmount));
		String expectedSurcharges=String.format("%.2f", Double.sum(subTotal*0.05,surcharges));
		String expectedGratuity=String.format("%.2f", Double.sum(subTotal*0.20,gratuity));
		orderPage.sendAndProceed();
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		tablePage.isTableVisible();
		dashboardPage=tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		Assert.assertEquals(dashboardPage.getSales(), expectedSalesAmount,"Sales Amount Unequal: ");
		dashboardPage.openOverview();
		Assert.assertEquals(dashboardPage.getActualSales(), expectedActualSalesAmount,"Expected Sales Amount Unequal: ");
		Assert.assertEquals(dashboardPage.getTotalSales(), expectedTotalSalesAmount,"Expected Sales Amount Unequal: ");
		Assert.assertEquals(dashboardPage.getTotalChecks(), totalChecks+1,"Total Checks Unequal: ");
		Assert.assertEquals(dashboardPage.getGuestsServed(), guestsServed+5,"Unequal count of guests served: ");
		dashboardPage.openZReport();
		Assert.assertEquals(dashboardPage.getNetSales(), expectedNetSales,"Net Sales not equal: ");
		Assert.assertEquals(dashboardPage.getTaxes(), expectedTax,"Taxes not equal: ");
		Assert.assertEquals(dashboardPage.getCashAmount(), expectedCashAmount,"Cash Amount not equal: ");
		Assert.assertEquals(dashboardPage.getSurcharges(), expectedSurcharges,"Surcahrges not equal: ");
		Assert.assertEquals(dashboardPage.getGratuity(), expectedGratuity,"Gratuity not equal: ");
	}
	
	@Test(groups= {"Smoke"})
	public void verifyVoids() {
		enterPin_openDashboard();
		dashboardPage.openOverview();
		double voidedBefore=Double.parseDouble(dashboardPage.getVoided());
		dashboardPage.openZReport();
		dashboardPage.scrollTo("Void Item Count","android.widget.ScrollView");
		double voidAmountBefore=Double.parseDouble(dashboardPage.getVoidAmount());
		int orderCountBefore=dashboardPage.getVoidOrderCount();
		int itemCountBefore=dashboardPage.getVoidItemCount();
		dashboardPage.gotoTableServe();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.waitForSubtotal();
		double subTotalAmount=orderPage.getSubTotalAmount();
		int items=orderPage.getItemsCount();
		String expectedAmount=String.format("%.2f",Double.sum(voidAmountBefore, subTotalAmount));
		String expectedVoided=String.format("%.2f",Double.sum(voidedBefore, subTotalAmount));
		orderPage.openKebabMenu();
		orderPage.voidTicket();
		orderPage.waitForTables();
		dashboardPage=tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		dashboardPage.openOverview();
		String actualVoided=dashboardPage.getVoided();
		Assert.assertEquals(actualVoided, expectedVoided,"Voided Not Equal on Overview: ");
		dashboardPage.openZReport();
		dashboardPage.scrollTo("Void Percent","android.widget.ScrollView");
		String voidAmountAfter=dashboardPage.getVoidAmount();
		Assert.assertEquals(voidAmountAfter, expectedAmount,"Voided Amount not Equal: ");
		Assert.assertEquals(dashboardPage.getVoidOrderCount(), orderCountBefore+1,"Voided Order Count not Equal: ");
		Assert.assertEquals(dashboardPage.getVoidItemCount(), itemCountBefore+items,"Voided Item Count not Equal: ");
	}
	
	@Test(groups= {"Smoke"})
	public void verifyRefunds() {
		enterPin_openDashboard();
		dashboardPage.openOverview();
		double refunds=Double.parseDouble(dashboardPage.getRefunds());
		System.out.println("Refunds Before: "+refunds);
		dashboardPage.gotoTableServe();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.waitForSubtotal();
		double subTotalAmount=orderPage.getSubTotalAmount();
		System.out.println("Subtotal: "+subTotalAmount);
		double taxOnSubTotal=roundHalfEven(subTotalAmount*0.09375);
		System.out.println("TAX ON SUBTOTAL: "+taxOnSubTotal);
		subTotalAmount=Double.sum(subTotalAmount, taxOnSubTotal);
		double subTotalWithTax=roundHalfUp(subTotalAmount);
		System.out.println("SUBTOTAL With Tax: "+taxOnSubTotal);
		double totalAmount=orderPage.getTotalAmount();
		double serviceCharge=orderPage.getServiceCharges();
		double serviceChargeTax=roundHalfUp(serviceCharge*0.09375);
		double excludeService=totalAmount-serviceCharge;
		double excludeServiceTax=roundHalfUp(excludeService-serviceChargeTax);
		orderPage.sendAndProceed();
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		tablePage.isTableVisible();
		dashboardPage=tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		double sales=Double.parseDouble(dashboardPage.getSales());
		dashboardPage.openOverview();
		double actualSales=Double.parseDouble(dashboardPage.getActualSales());
		System.out.println("Actual Sales: "+actualSales);
		double totalSales=Double.parseDouble(dashboardPage.getTotalSales());
		dashboardPage.openZReport();
		double totalSalesZ=Double.parseDouble(dashboardPage.getTaxes());
		double cashAmount=Double.parseDouble(dashboardPage.getCashAmount());
		ordershubPage=tablePage.gotoOrdershub();
		ordershubPage.openEntry(ticketId);
		ordershubPage.gotoPayment();
		ordershubPage.gotoRefund();
		double refundPrice=Double.parseDouble(ordershubPage.getRefundPrice());
		double itemRefundTotal=Double.parseDouble(ordershubPage.getItemRefundTotal());
		Assert.assertEquals(refundPrice,subTotalWithTax,"Refund Amount incorrect on Refund Screen: ");
		ordershubPage.tapPaymentNextBtn();
		ordershubPage.selectReasonAndConfirm();
		ordershubPage.noReceipt();
		Assert.assertTrue(ordershubPage.isTextDisplayed(),"Refund status not displayed");
		String expectedSales=String.format("%.2f", sales-refundPrice);
		System.out.println("EXPECTED SALES: "+expectedSales);
		String expectedActualSales=String.format("%.2f", (actualSales-itemRefundTotal));
		System.out.println("EXPECTED ACTUAL SALES: "+expectedActualSales);
		String expectedTotalSales=String.format("%.2f", totalSales-itemRefundTotal);
		String expectedSalesZ=String.format("%.2f", totalSalesZ-itemRefundTotal);
		String expectedCash=String.format("%.2f", cashAmount-itemRefundTotal);
		dashboardPage=ordershubPage.gotoDashboard();
		Assert.assertEquals(dashboardPage.getSales(), expectedSales,"Sales Amount incorrect on Dashboard: ");
		dashboardPage.openOverview();
		Assert.assertEquals(dashboardPage.getActualSales(), expectedActualSales,"Actual Sales Amount incorrect on Overview tab: ");
		Assert.assertEquals(dashboardPage.getTotalSales(), expectedTotalSales,"Total Sales Amount incorrect on Overview tab: ");
		String refundsAfter=dashboardPage.getRefunds();
		String calculated=String.format("%.2f",Double.sum(refunds,excludeServiceTax));
		Assert.assertEquals(refundsAfter, calculated,"Refunds Amount Unequal: ");
		dashboardPage.openZReport();
		Assert.assertEquals(dashboardPage.getTotalSalesOnZReport(), expectedSalesZ,"Total Sales incorrect on Z Report: ");
		Assert.assertEquals(dashboardPage.getCashAmount(), expectedCash,"Cash Amount incorrect on Z Report: ");
	}
	
	@Test(groups= {"Smoke"})
	public void verifyUnpaidChecks() {
		enterPin_openDashboard();
		dashboardPage.openZReport();
		int unclosedChecksBefore=dashboardPage.getUnclosedChecks();
		dashboardPage.gotoTableServe();
		addGuestsAndItem("5");
		orderPage.sendToKDS();
		dashboardPage=tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		dashboardPage.openZReport();
		int unclosedChecksAfter=dashboardPage.getUnclosedChecks();
		Assert.assertEquals(unclosedChecksAfter, unclosedChecksBefore+1,"Unclosed checks count incorrect: ");
	}
	
	@Test(groups= {"Smoke"})
	public void verifyVoidItem() {
		enterPin_openDashboard();
		dashboardPage.openZReport();
		dashboardPage.scrollTo("Void Percent","android.widget.ScrollView");
		double voidAmountBefore=Double.parseDouble(dashboardPage.getVoidAmount());
		int voidItemCountBefore=dashboardPage.getVoidItemCount();
		dashboardPage.gotoTableServe();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.waitForSubtotal();
		double firstItemPrice=orderPage.getFirstItemPrice();
		orderPage.addSecondItem();
		orderPage.isItemAdded(2);
		double secondItemPrice=orderPage.getSecondItemPrice();
		orderPage.stay();
		orderPage.voidItem();
		orderPage.waitForSubtotal(Double.toString(secondItemPrice));
		orderPage.pay();
		orderPage.waitForPaymentTicket();
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		tablePage.isTableVisible();
		dashboardPage=tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		dashboardPage.openZReport();
		dashboardPage.scrollTo("Void Percent","android.widget.ScrollView");
		double expectedVoidAmount=roundHalfUp(Double.sum(voidAmountBefore,firstItemPrice));
		Assert.assertEquals(Double.parseDouble(dashboardPage.getVoidAmount()), expectedVoidAmount,"Void Amount Incorrect After Void Item: ");
		Assert.assertEquals(dashboardPage.getVoidItemCount(), voidItemCountBefore+1,"Void Item Count Incorrect After Void Item: ");
	}
	
	@Test(groups= {"Smoke"})
	public void verfiyComps() {
		enterPin_openDashboard();
		dashboardPage.openOverview();
		double compsBefore=dashboardPage.getTotalComps();
		dashboardPage.gotoTableServe();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.waitForSubtotal();
		double subtotal=orderPage.getSubTotalAmount();
		orderPage.isItemAdded(1);
		orderPage.openKebabMenu();
		orderPage.addPercentCheckDiscount();
		orderPage.waitForDisount();
		orderPage.sendAndProceed();
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		tablePage.isTableVisible();
		dashboardPage=tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		dashboardPage.openOverview();
		double expectedComps=Double.sum(compsBefore, roundHalfUp(subtotal*0.1));
		Assert.assertEquals(dashboardPage.getTotalComps(), expectedComps,"Comps Incorrect on Dashboard: ");
	}
	
	@Test(groups= {"Smoke"})
	public void verfiyCompsAfterAmountDiscount() {
		enterPin_openDashboard();
		dashboardPage.openOverview();
		double compsBefore=dashboardPage.getTotalComps();
		dashboardPage.gotoTableServe();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.openKebabMenu();
		orderPage.addDollarsCheckDiscount();
		orderPage.waitForDisount();
		orderPage.sendAndProceed();
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		tablePage.isTableVisible();
		dashboardPage=tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		dashboardPage.openOverview();
		double expectedComps=Double.sum(compsBefore, 10);
		Assert.assertEquals(dashboardPage.getTotalComps(), expectedComps,"Comps Incorrect on Dashboard: ");
	}
	
		
	@Test(groups = {"Smoke"})
	public void verifyPartialRefund()
	{
		enterPin_openDashboard();
		dashboardPage.openOverview();
		double refunds=Double.parseDouble(dashboardPage.getRefunds());
		System.out.println("Refunds Before: "+refunds);
		dashboardPage.gotoTableServe();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		orderPage.waitForSubtotal();
		double subTotalAmount=orderPage.getSubTotalAmount();
		System.out.println("Subtotal: "+subTotalAmount);
		double taxOnSubTotal=roundHalfEven(subTotalAmount*0.09375);
		System.out.println("TAX ON SUBTOTAL: "+taxOnSubTotal);
		subTotalAmount=Double.sum(subTotalAmount, taxOnSubTotal);
		double subTotalWithTax=roundHalfUp(subTotalAmount);
		System.out.println("SUBTOTAL With Tax: "+taxOnSubTotal);
		double totalAmount=orderPage.getTotalAmount();
		double serviceCharge=orderPage.getServiceCharges();
		double serviceChargeTax=roundHalfUp(serviceCharge*0.09375);
		double excludeService=totalAmount-serviceCharge;
		double excludeServiceTax=roundHalfUp(excludeService-serviceChargeTax);
		orderPage.sendAndProceed();
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		tablePage.isTableVisible();
		dashboardPage=tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		dashboardPage.openOverview();
		double actualSales=Double.parseDouble(dashboardPage.getActualSales());
		System.out.println("Actual Sales: "+actualSales);
		ordershubPage=tablePage.gotoOrdershub();
		ordershubPage.openEntry(ticketId);
		ordershubPage.gotoPayment();
		ordershubPage.gotoRefund();
		ordershubPage.refundPartial();
		
		double totalRefundPrice = Double.parseDouble(ordershubPage.getTotalRefundAmount());
		double refundPrice = Double.parseDouble(ordershubPage.getItemRefundTotal());
		
		ordershubPage.tapPaymentNextBtn();
		ordershubPage.selectReasonAndConfirm();
		ordershubPage.noReceipt();
		System.out.println("Refund Price: " + refundPrice);
		double actualSalesAfter = roundHalfUp(actualSales - refundPrice);
		System.out.println("Actual Sales After: " + actualSalesAfter);
		double refundAfter = roundHalfUp(Double.sum(totalRefundPrice, refunds));
		System.out.println("Refund After: " + refundPrice);
		
		dashboardPage=tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		dashboardPage.openOverview();
		
		
		Assert.assertEquals(Double.parseDouble(dashboardPage.getActualSales()), actualSalesAfter, "Actual Sales are not equal");
		Assert.assertEquals(Double.parseDouble(dashboardPage.getRefunds()), refundAfter, "Actual Refunds are not equal");
		
	}
	
	@Test(groups = {"Smoke"})
	public void verifyTenders() {
		enterPin_openDashboard();
		dashboardPage.openZReport();
		double tenderPayment = dashboardPage.getTenderAmount();
		System.out.println(tenderPayment);
		dashboardPage.gotoTableServe();
		addGuestsAndItem("5");
		orderPage.isItemAdded(1);
		double itemAmount = orderPage.getTotalAmount();
		double expectedTenderAmount = roundHalfUp(Double.sum(tenderPayment, itemAmount));
		orderPage.sendAndProceed();
		orderPage.waitForPaymentTicket();
		orderPage.tapTenderButton();
		PaymentPage paymentPage = orderPage.tapServerWithoutTip();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		tablePage.isTableVisible();
		DashboardPage dashboardPage = tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		dashboardPage.openZReport();
		Assert.assertEquals(dashboardPage.getTenderAmount(), expectedTenderAmount, "Tender Amount is not equal");
	}
	
	private void enterPin_openDashboard() {
		enterPin();
		isTableScreenDisplayed();
		dashboardPage=tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
	}
}
