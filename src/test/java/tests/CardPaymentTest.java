package tests;

import java.net.URL;
import java.time.Duration;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import baseTest.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import pages.DashboardPage;
import pages.OrderPage;
import pages.OrdershubPage;
import pages.PaymentPage;
import pages.TablePage;

public class CardPaymentTest {
	
	public AndroidDriver driver;
	public OrderPage orderPage;
	public TablePage tablePage;
	public BaseTest baseTest;
	public PaymentPage paymentPage;
	public OrdershubPage orderHub;
	public DashboardPage dashboardPage;	
	
	@BeforeClass
	@Parameters({"udid","systemPort", "app"})
	public void beforeClass(String udid,int systemPort,String app) {
		baseTest = new BaseTest();
		UiAutomator2Options options=new UiAutomator2Options();
		options.setCapability("udid",udid);
		options.setAppPackage("aio.app.pos."+app);
		options.setAppActivity("aio.app.pos.ui.main.ClockInActivity");
		options.setNoReset(true);
		options.setCapability("autoGrantPermissions",true);
		options.setNewCommandTimeout(Duration.ofSeconds(600));
		options.setSystemPort(systemPort);
		try {
		driver=new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
		}
		catch(Exception e) {
			e.printStackTrace();
			}
		tablePage = new TablePage(driver);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	}


	@Test
	public void partialCardPaymentWithTipLater() {
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("5");
		String ticketIDBefore = orderPage.getTicketId();
		orderPage.addItem();
		orderPage.sendAndProceed();
		double balanceDue=orderPage.getBalanceDuePayment();
		orderPage.splitPaymentAmount();
		double paymentAmount=orderPage.getPaymentAmount();
		double calculated=baseTest.roundHalfUp(balanceDue/2);
		Assert.assertEquals(paymentAmount,calculated,"Payment Amount incorrect after payment split: ");
		double expectedBalanceDue=baseTest.roundHalfUp(balanceDue-paymentAmount);
		paymentPage = orderPage.tapOnCard();
		paymentPage.waitForPaymentComplete();
		paymentPage.addTipLater();
		paymentPage.noReceipt();
		Assert.assertEquals(orderPage.getBillStatus(), "Partial","Partial Payment Status incorrect after partial payment: ");
		System.out.println(orderPage.getBalanceDuePayment());
		Assert.assertEquals(orderPage.getBalanceDuePayment(),expectedBalanceDue,"Balance Due Incorrect after partial payment: ");
		paymentPage = orderPage.tapOnCard();
		paymentPage.waitForPaymentComplete();
		paymentPage.addTipLater();
		paymentPage.noReceipt();
		tablePage.isTableVisible();
		orderHub = tablePage.gotoOrdershub();
		orderHub.openUnclosedTab();
		String ticketIDAfter = orderHub.getTicketID();
		Assert.assertEquals(ticketIDBefore, ticketIDAfter, "Ticket ID is not equal");
	}
	
	@Test
	public void refundCardPayment() {
		//orderHub.gotoTableServe();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("2");
		orderPage.addItem();
		double totalAmountBefore = orderPage.getTotalAmount();
		double subTotalAmountBefore = orderPage.getSubTotalAmount();
		double serviceChargeBefore = orderPage.getServiceCharges();
		double taxBefore = orderPage.getTax();
		orderPage.sendAndProceed();
		paymentPage = orderPage.tapOnCard();
		paymentPage.waitForPaymentComplete();
		paymentPage.skipTip();
		paymentPage.waitForNoReceipt();
		dashboardPage = tablePage.openDashboard();
		dashboardPage.openOverview();
		double refundsBefore = Double.parseDouble(dashboardPage.getRefunds());
		double salesBefore = Double.parseDouble(dashboardPage.getTotalSales());
		orderHub = dashboardPage.gotoOrdershub();
		orderHub.openFirstEntry();
		orderHub.gotoPayment();
		orderHub.gotoRefund();
		double refundSalesTax = Double.parseDouble(orderHub.getRefundSalesTax());
		double taxOnServiceCharge = baseTest.roundHalfUp(serviceChargeBefore * 0.09375);
		double calculatedRefundTax = baseTest.roundHalfUp(taxBefore - taxOnServiceCharge); 
		double calculatedRefundAmount = baseTest.roundHalfUp((totalAmountBefore * 0.0374) + 0.08);
		double refundAmount = subTotalAmountBefore - calculatedRefundAmount;
		double refundAmountBefore = Double.parseDouble(orderHub.getItemRefundTotal());
		Assert.assertEquals(refundAmountBefore, refundAmount, "Refund Amount is not equal");
		Assert.assertEquals(refundSalesTax, calculatedRefundTax, "Expected and Actual Tax is not equal");
		double refundPrice = Double.parseDouble(orderHub.getRefundPrice());
		double totalRefund = (refundsBefore + refundPrice); 
		double totalSales = (salesBefore -  refundAmountBefore);
		orderHub.tapPaymentNextBtn();
		orderHub.selectReasonAndConfirm();
		orderHub.noReceipt();
		dashboardPage = orderHub.gotoDashboard();
		dashboardPage.openOverview();
		double refundsAfter = Double.parseDouble(dashboardPage.getRefunds());
		double salesAfter = Double.parseDouble(dashboardPage.getTotalSales());
		Assert.assertEquals(totalRefund,  refundsAfter, "Total Refund is not equal");
		Assert.assertEquals(totalSales,  salesAfter, "Total Sales are not equal");
	}
	
	@Test
	public void partialRefundCardPayment() {
		//dashboardPage.gotoTableServe();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("2");
		orderPage.addItem();
		orderPage.sendAndProceed();
		paymentPage = orderPage.tapOnCard();
		paymentPage.waitForPaymentComplete();
		paymentPage.skipTip();
		paymentPage.waitForNoReceipt();
		dashboardPage = tablePage.openDashboard();
		dashboardPage.openOverview();
		double BeforePartialRefund = Double.parseDouble(dashboardPage.getRefunds());
		orderHub = dashboardPage.gotoOrdershub();
		orderHub.openFirstEntry();
		orderHub.gotoPayment();
		orderHub.gotoRefund();
		double getRefundAmount = Double.parseDouble(orderHub.getItemRefundTotal());
		double getrefundSalesTax = Double.parseDouble(orderHub.getRefundSalesTax());
		orderHub.refundPartial();
		double getPartailRefundAmount = Double.parseDouble(orderHub.getItemRefundTotal());
		double getPartailRefundSalesTax = Double.parseDouble(orderHub.getRefundSalesTax());
		double getTotalRefundsAmount = Double.parseDouble(orderHub.getTotalRefundAmount());
		double PercentPartialTax = (getrefundSalesTax / getRefundAmount) * 100;
		double PartialTax = baseTest.roundHalfUp((PercentPartialTax * getPartailRefundAmount) / 100);
		Assert.assertEquals(getPartailRefundSalesTax, PartialTax, "Refund Sales Tax is not equal");
		double PartialRefundAmount = getPartailRefundAmount + getPartailRefundSalesTax ;
		Assert.assertEquals(PartialRefundAmount, getTotalRefundsAmount, "Partial Refund Amount is not equal");
		orderHub.tapPaymentNextBtn();
		orderHub.selectReasonAndConfirm();
		orderHub.noReceipt();
		dashboardPage = tablePage.openDashboard();
		dashboardPage.openOverview();
		double AfterPartialRefund = Double.parseDouble(dashboardPage.getRefunds());
		double ActualRefund = baseTest.roundHalfUp(BeforePartialRefund + PartialRefundAmount);
		Assert.assertEquals(ActualRefund, AfterPartialRefund, "ActualRefund and AfterPartialRefund is not Equal");
//		System.out.println("-----------test-----: " + ActualRefund);
	}
	
	@Test
	public void totalTips() {
		//dashboardPage.gotoTableServe();
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("2");
		String ticketIDBefore = orderPage.getTicketId();
		orderPage.addItem();
		orderPage.sendAndProceed();
		paymentPage = orderPage.tapOnCard();
		paymentPage.waitForPaymentComplete();
		paymentPage.addTipLater();
		paymentPage.waitForNoReceipt();
		tablePage.isTableVisible();
		dashboardPage = tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		dashboardPage.openOverview();
		double totalTipAmountBefore=dashboardPage.getTotalTips();
		System.out.println(totalTipAmountBefore);
		orderHub = dashboardPage.gotoOrdershub();
		orderHub.openUnclosedTab();
		String ticketIDAfter = orderHub.getTicketID();
		Assert.assertEquals(ticketIDBefore, ticketIDAfter, "Ticket ID is not equal");
		orderHub.openEntry(ticketIDAfter);
		orderHub.addATipBtn();
		orderHub.enteringTip();
		orderHub.waitForInvisibility();
		dashboardPage = orderHub.openDashboard();
		dashboardPage.openOverview();
		double totalTipAmountAfter=dashboardPage.getTotalTips();
		System.out.println(totalTipAmountAfter);
		double additionalTip = 10;
		double actualTotalTipsAmount = baseTest.roundHalfUp(Double.sum(totalTipAmountBefore, additionalTip));
		System.out.println(actualTotalTipsAmount);
		Assert.assertEquals(actualTotalTipsAmount,totalTipAmountAfter, "Total tips amount mismatch!");

	}
	@Test
	public void overAllTipPercent() {
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("2");
		String ticketIDBefore = orderPage.getTicketId();
		orderPage.addItem();
		orderPage.sendAndProceed();
		paymentPage = orderPage.tapOnCard();
		paymentPage.waitForPaymentComplete();
		paymentPage.addTipLater();
		paymentPage.waitForNoReceipt();
		tablePage.isTableVisible();
		orderHub= tablePage.gotoOrdershub();
		orderHub.openUnclosedTab();
		String ticketIDAfter = orderHub.getTicketID();
		orderHub.openEntry(ticketIDAfter);
		orderHub.addATipBtn();
		orderHub.enteringTip();
		orderHub.waitForInvisibility();
		dashboardPage = orderHub.openDashboard();
		dashboardPage.openOverview();
		double TotalSales= dashboardPage.getTotalSaleAmount();
		System.out.println(TotalSales);
		double totalTipAmount=dashboardPage.getTotalTips();
		System.out.println(totalTipAmount);
		double ActualTip= baseTest.roundHalfUp((totalTipAmount/TotalSales)*100);
		System.out.println(ActualTip);
		double ExpectedTip = dashboardPage.getOverAllTipPercent();
		System.out.println(ExpectedTip);
		Assert.assertEquals(ActualTip,ExpectedTip, "The Tip Percentage are not equal!");
		double balanceDue=orderPage.getBalanceDuePayment();
		orderPage.splitPaymentAmount();
		double paymentAmount=orderPage.getPaymentAmount();
		double calculated=baseTest.roundHalfUp(balanceDue/2);
		Assert.assertEquals(paymentAmount,calculated,"Payment Amount incorrect after payment split: ");
		double expectedBalanceDue=baseTest.roundHalfUp(balanceDue-paymentAmount);
		paymentPage = orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		Assert.assertEquals(orderPage.getBillStatus(), "Partial","Partial Payment Status incorrect after partial payment: ");
		System.out.println(orderPage.getBalanceDuePayment());
		Assert.assertEquals(orderPage.getBalanceDuePayment(),expectedBalanceDue,"Balance Due Incorrect after partial payment: ");
		paymentPage = orderPage.tapOnCard();
		paymentPage.waitForPaymentComplete();
		paymentPage.addTipLater();
		paymentPage.noReceipt();
		Assert.assertTrue(tablePage.isTableVisible(),"Not Visible");
	}
	
	@Test
	public void partialCashAndPartialCard() {
		String tableNum = tablePage.findEmptyTable();
		tablePage.openTable(tableNum);
		orderPage = tablePage.addGuestsInTable("2");
		String ticketIDBefore = orderPage.getTicketId();
		orderPage.addItem();
		orderPage.sendAndProceed();
		double balanceDue=orderPage.getBalanceDuePayment();
		orderPage.splitPaymentAmount();
		double paymentAmount=orderPage.getPaymentAmount();
		double calculated=baseTest.roundHalfUp(balanceDue/2);
		Assert.assertEquals(paymentAmount,calculated,"Payment Amount incorrect after payment split: ");
		double expectedBalanceDue=baseTest.roundHalfUp(balanceDue-paymentAmount);
		paymentPage = orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		Assert.assertEquals(orderPage.getBillStatus(), "Partial","Partial Payment Status incorrect after partial payment: ");
		System.out.println(orderPage.getBalanceDuePayment());
		Assert.assertEquals(orderPage.getBalanceDuePayment(),expectedBalanceDue,"Balance Due Incorrect after partial payment: ");
		paymentPage = orderPage.tapOnCard();
		paymentPage.waitForPaymentComplete();
		paymentPage.addTipLater();
		paymentPage.noReceipt();
		Assert.assertTrue(tablePage.isTableVisible(),"Not Visible");
	}
	
}
