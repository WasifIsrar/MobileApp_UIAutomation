package tests;



import org.testng.Assert;
import org.testng.annotations.Test;
import baseTest.POSBaseTest;



public class CashDrawerTest extends POSBaseTest{
	
	private String thisStation="This Station";
	private String houseDrawer="House";
	
	@Test(groups= {"Smoke"})
	public void verifyDeclaredCash() {
		enterPin_openDrawer(thisStation);
		double totalBalanceBefore=cashDrawerPage.getTotalBalance();
		System.out.println("Total balance before declaring cash:"+totalBalanceBefore);
		cashDrawerPage.gotoTableServe();
		dashboardPage=tablePage.openDashboard();
		dashboardPage.isPageDisplayed();
		dashboardPage.declareCash();
		dashboardPage.goBack();
		tablePage.gotoCashDrawer();
		cashDrawerPage.openDrawer(thisStation);
		double totalBalanceAfter=cashDrawerPage.getTotalBalance();
		System.out.println("Total balance after declaring cash:"+totalBalanceAfter);
		double calculatedAmount=totalBalanceBefore+10;
		String formattedCalculatedAmount;
		if(calculatedAmount%1.0==0) {
		    formattedCalculatedAmount = String.format("%d", (int) calculatedAmount);
		}
		else {
		    formattedCalculatedAmount = String.format("%.2f", calculatedAmount);
		}
		Assert.assertEquals(totalBalanceAfter, Double.parseDouble(formattedCalculatedAmount),"Amount Unequal After Declare Cash");
	}
	
	@Test(groups= {"Smoke"})
	public void closeDrawerWithEqualAmount() {
		enterPin_openDrawer(houseDrawer);
		double houseBalanceBefore=cashDrawerPage.getTotalBalance();
		System.out.println("House Balance Before:"+houseBalanceBefore);
		cashDrawerPage.goBack();
		int reopenBtnStation=cashDrawerPage.openDrawer(thisStation);
		if(reopenBtnStation>0) {
			cashDrawerPage.reopenDrawer();
		}
		double totalBalanceBefore=cashDrawerPage.getTotalBalance();
		System.out.println("Total Balance Before:"+totalBalanceBefore);
		String amount=cashDrawerPage.verifyDrawerClosed(Double.toString(totalBalanceBefore));
		System.out.println("Amount:"+amount);
		Assert.assertTrue(cashDrawerPage.getLog().contains("Close Drawer"),"Cashdrawer log incorrect");
		Assert.assertEquals(cashDrawerPage.getTotalBalance(), 0,"Amount unequal after Closing Drawer with Equal Amount: ");
		cashDrawerPage.goBack();
		cashDrawerPage.openDrawer(houseDrawer);
		double expectedBalance=roundHalfUp(Double.sum(houseBalanceBefore, totalBalanceBefore));
		Assert.assertEquals(cashDrawerPage.getTotalBalance(), expectedBalance,"The amounts are not equal:");
		System.out.println("Expected Balance"+expectedBalance);
		Assert.assertTrue(cashDrawerPage.getLog().contains("Close Drawer"),"Log Incorrect in House Drawer");
	}
	
	@Test(groups= {"Smoke"})
	public void closeDrawerWithLessAmount() {
		enterPin_openDrawer(houseDrawer);
		double houseBalanceBefore=cashDrawerPage.getTotalBalance();
		System.out.println("Balance Before"+houseBalanceBefore);
		cashDrawerPage.goBack();
		int reopenBtnStation=cashDrawerPage.openDrawer(thisStation);
		if(reopenBtnStation>0) {
			cashDrawerPage.reopenDrawer();
		}
		cashDrawerPage.adjust();
		String balanceAfter=cashDrawerPage.getBalanceAfter();
		System.out.println("Balance After"+balanceAfter);
		cashDrawerPage.verifyDrawerClosed("");
		Assert.assertTrue(cashDrawerPage.getLog().contains("Close Drawer"),"Cashdrawer log incorrect: ");
		Assert.assertEquals(cashDrawerPage.getBalanceAfter(), "0.00","Balance After Incorrect: ");
		Assert.assertEquals(cashDrawerPage.getTotalBalance(), 0,"Total balance Incorrect: ");
		Assert.assertEquals(cashDrawerPage.getComments(), "Underage of $"+balanceAfter,"Amount unequal after Closing with Less Amount: ");
		cashDrawerPage.goBack();
		cashDrawerPage.openDrawer(houseDrawer);
		Assert.assertTrue(cashDrawerPage.getLog().contains("Close Drawer"),"Log Incorrect in House Drawer: ");
		Assert.assertEquals(cashDrawerPage.getComments(), "Underage of $"+balanceAfter,"Comments Incorrect: ");
	}
	
	@Test(groups= {"Smoke"})
	public void closeDrawerWithMoreAmount() {
		enterPin_openDrawer(houseDrawer);
		double houseBalanceBefore=cashDrawerPage.getTotalBalance();
		System.out.println("Balance Before"+houseBalanceBefore);
		cashDrawerPage.goBack();
		int reopenBtnStation=cashDrawerPage.openDrawer(thisStation);
		if(reopenBtnStation>0) {
			cashDrawerPage.reopenDrawer();
		}
		double totalBalanceBefore=cashDrawerPage.getTotalBalance();
		double totalBalance=Double.sum(totalBalanceBefore,10.00);
		String total=String.format("%.2f", totalBalance);
		System.out.println("Total"+totalBalanceBefore);
		double expected = roundHalfUp(Double.sum(houseBalanceBefore, totalBalanceBefore));
		System.out.println("Expected:"+expected);
		cashDrawerPage.verifyDrawerClosed(total);
		Assert.assertTrue(cashDrawerPage.getLog().contains("Close Drawer"),"Log Incorrect");
		String balanceAfter=cashDrawerPage.getBalanceAfter();
		Assert.assertEquals(balanceAfter, total,"Balance After Incorrect: ");
		Assert.assertEquals(cashDrawerPage.getTotalBalance(), 0,"Total Balance Incorrect: ");
		Assert.assertEquals(cashDrawerPage.getComments(), "Overage of $10.00","Amount Unequal After Closing with More Amount");
		cashDrawerPage.goBack();
		cashDrawerPage.openDrawer(houseDrawer);
		Assert.assertEquals(cashDrawerPage.getTotalBalance(),expected,"Total Balance Incorrect on House Drawer: ");
		Assert.assertTrue(cashDrawerPage.getLog().contains("Close Drawer"),"Log Incorrect in House Drawer: ");
		Assert.assertEquals(cashDrawerPage.getBalanceAfter(),balanceAfter,"Balance After Unequal on This Station and House Drawer: ");
		Assert.assertEquals(cashDrawerPage.getComments(), "Overage of $10.00","Comments Incorrect: ");
	}
	
	@Test(groups= {"Smoke"})
	public void adjustStartingBalance() {
		enterPin_openDrawer(thisStation);
		cashDrawerPage.adjust();
		Assert.assertEquals(cashDrawerPage.getLog(), "Adjust Starting Balance","Log Incorrect");
		Assert.assertEquals(cashDrawerPage.getBalanceAfter(), "10.00","Balance After Incorrect");
		Assert.assertEquals(cashDrawerPage.getTotalBalance(), 10,"Total Balance Incorrect");
	}
	
	@Test(groups= {"Smoke"})
	public void adjustStartingBalanceInHouseDrawer() {
		enterPin_openDrawer(houseDrawer);
		cashDrawerPage.adjust();
		Assert.assertEquals(cashDrawerPage.getTotalBalance(),10,"Total Balance Incorrect After Closing House Drawer: ");
		Assert.assertEquals(cashDrawerPage.getLog(), "Adjust Starting Balance","Log Incorrect");
		Assert.assertEquals(cashDrawerPage.getBalanceAfter(), "10.00","Balance After Incorrect");
	}
	
	@Test(groups= {"Smoke"})
	public void closeHouseDrawerWithEqualAmount() {
		enterPin_openDrawer(houseDrawer);
		double totalBalanceBefore=cashDrawerPage.getTotalBalance();
		String amount=cashDrawerPage.verifyDrawerClosed(Double.toString(totalBalanceBefore));
		System.out.println("amount"+amount);
		Assert.assertTrue(cashDrawerPage.getLog().contains("Close Drawer"),"Cashdrawer log incorrect");
		System.out.println("balance after:"+cashDrawerPage.getBalanceAfter());
		Assert.assertEquals(Double.parseDouble(cashDrawerPage.getBalanceAfter()), Double.parseDouble(amount),"Balance After Incorrect");
		Assert.assertEquals(cashDrawerPage.getTotalBalance(),0,"Amount unequal after Closing Drawer with Equal Amount");
	}
	
	@Test(groups= {"Smoke"})
	public void closeHouseDrawerWithLessAmount() {
		enterPin_openDrawer(houseDrawer);
		String balanceAfter=cashDrawerPage.getBalanceAfter();
		cashDrawerPage.verifyDrawerClosed("");
		Assert.assertTrue(cashDrawerPage.getLog().contains("Close Drawer"),"Cashdrawer log incorrect: ");
		Assert.assertEquals(cashDrawerPage.getBalanceAfter(), "0.00","Balance After Incorrect: ");
		Assert.assertEquals(cashDrawerPage.getTotalBalance(),0,"Amount unequal after Closing Drawer with 0: ");
	}
	
	@Test(groups= {"Smoke"})
	public void closeHouseDrawerWithMoreAmount() {
		enterPin_openDrawer(houseDrawer);
		double totalBalanceBefore=cashDrawerPage.getTotalBalance();
		String expected=(String.format("%.2f",Double.sum(totalBalanceBefore,10.00)));
		String[] amount=expected.split("\\.");
		int expectedAmount=0;
		if(amount[1].equals("00")) {
			expectedAmount=Integer.parseInt(amount[0]);
		}
		System.out.println("Expected Amount:"+expectedAmount);
		cashDrawerPage.verifyDrawerClosed(expected);
		Assert.assertTrue(cashDrawerPage.getLog().contains("Close Drawer"),"Cashdrawer log incorrect: ");
		System.out.println("Balance After:"+cashDrawerPage.getBalanceAfter());
		Assert.assertEquals(cashDrawerPage.getBalanceAfter(), expected,"Balance After Incorrect: ");
		Assert.assertEquals(cashDrawerPage.getTotalBalance(),0,"Amount unequal after Closing Drawer with more amount: ");
		Assert.assertEquals(cashDrawerPage.getComments(), "Overage of $"+"10.00","Comments Incorrect: ");
	}
	
	@Test(groups= {"Smoke"})
	public void verifyRefundInDrawer() {
		enterPin_openDrawer(thisStation);
		cashDrawerPage.goBacktoTableServe();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.sendAndProceed();
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		paymentPage.isTableVisible();
		tablePage.gotoCashDrawer();
		cashDrawerPage.openDrawer(thisStation);
		double totalBalanceBefore=cashDrawerPage.getTotalBalance();
		System.out.println("Total Balance Before:"+totalBalanceBefore);
		cashDrawerPage.gotoTableServe();
		ordershubPage=tablePage.gotoOrdershub();
		ordershubPage.openEntry(ticketId);
		ordershubPage.gotoPayment();
		ordershubPage.gotoRefund();
		ordershubPage.gotoRefundAmount();
		double refundedAmount=Double.parseDouble(ordershubPage.getTotalRefundAmount());
		System.out.println("Refunded Amount:"+refundedAmount);
		ordershubPage.tapPaymentNextBtn();
		ordershubPage.selectReasonAndConfirm();
		ordershubPage.noReceipt();
		ordershubPage.isTextDisplayed();
		cashDrawerPage=ordershubPage.gotoCashDrawer();
		cashDrawerPage.openDrawer(thisStation);
		Assert.assertEquals(cashDrawerPage.getTotalBalance(),roundHalfUp(totalBalanceBefore-refundedAmount),"Total Balance Incorrect After Refund: ");
		Assert.assertEquals(cashDrawerPage.getLog(),"Cash Refund","Log Incorrect: ");
		Assert.assertEquals(cashDrawerPage.getLoggedAmount(),Double.toString(refundedAmount),"Balance After Incorrect: ");
		Assert.assertEquals(cashDrawerPage.getComments(),"Cash Refund","Comment Incorrect: ");
	}
	@Test(groups= {"Smoke"})
	public void verifyCashLogs() {
		enterPin();
		isTableScreenDisplayed();
		addGuestsAndItem("5");
		orderPage.waitForSubtotal();
		double TicketAmountBefore=orderPage.getTotalAmount();
		System.out.println("Ticket Amount Before:"+TicketAmountBefore);
		orderPage.sendAndProceed();
		paymentPage=orderPage.payByCash();
		paymentPage.selectAmount();
		paymentPage.completePayment();
		paymentPage.noReceipt();
		paymentPage.isTableVisible();
		cashDrawerPage=tablePage.gotoCashLogs();
		cashDrawerPage.isCashLogsPageDisplayed();
		double TicketAmountAfter=Double.parseDouble(cashDrawerPage.getAmountofTicket());
		System.out.println("Ticket Amount After:"+TicketAmountAfter);
		Assert.assertEquals(TicketAmountBefore,TicketAmountAfter, "Amount incorrect:");
	}
	
	public void enterPin_openDrawer(String station) {
		enterPin();
		isTableScreenDisplayed();
		cashDrawerPage=tablePage.gotoCashDrawer();
		int reopenBtn=cashDrawerPage.openDrawer(station);
		if(reopenBtn>0) {
			cashDrawerPage.reopenDrawer();
		}
	}
}