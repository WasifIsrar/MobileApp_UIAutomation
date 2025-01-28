package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.google.common.collect.ImmutableMap;

import basePage.BasePage;
import io.appium.java_client.android.AndroidDriver;

public class KioskMenuPage extends BasePage{
	
	private By category=By.xpath("//android.widget.TextView[contains(@text, 'Signature Food Flights')]");
	private By foodItems=By.xpath("//androidx.recyclerview.widget.RecyclerView[contains(@resource-id, 'rvFoodItems')]//android.widget.FrameLayout[contains(@resource-id, 'itemView')]");
	private By addModifierBtn=By.id("btnAdd");
	private By addToCartBtn=By.id("tvAddToCart");
	private By itemPrice=By.xpath("//android.widget.TextView[contains(@resource-id, 'tvItemPrice')]");
	private By viewCartBtn=By.id("tvViewCart");
	private By removeBtn=By.id("tvRemove");
	private By updateCartBtn=By.id("btnUpdateOrder");
	private By modifierName=By.id("tvModifierName");
	private By customiseBtn=By.id("btnCustomized");
	private By specialRequestBtn=By.xpath("//android.widget.TextView[@text='Request']");
	private By requestField=By.id("etRequest");
	private By startNewBtn=By.id("startNew");
	private By yesBtn=By.id("tvYes");
	private By yourCart=By.id("tvYourCart");
	private By minusBtn=By.id("btnMinus");
	private By clearChanges = By.xpath("//android.widget.TextView[@text='Clear Changes']");
	private By signatureFood = By.xpath("//android.widget.TextView[@text='Signature Food Flights']");
	private By search = By.id("ivSearch");
	private By searcBar = By.id("etSearch");
	private By searchedItemList = By.xpath("//android.widget.TextView[contains(@resource-id, 'tvCategoryName')]");
	private By closeSearch = By.id("ivBack");
	private By itemPriceText = By.id("tvPrice");
	private By caesarSalad = By.xpath("//android.widget.TextView[contains(@text, 'Caesar Salad')]");
	private By saladAddOn = By.xpath("//android.widget.TextView[contains(@text, 'Salad Add on')]");

	public KioskMenuPage(AndroidDriver driver) {
		super(driver);
	}
	
	public void isPageDisplayed() {
		waitForElementVisibility(category,240);
	}
	
	public void addItem(int i) {
		driver.findElement(category).click();
		driver.findElements(foodItems).get(i).click();
	}
	
	public void addItemWithModifier() {
		driver.findElement(By.xpath("//android.widget.TextView[@text='Salads']")).click();
		driver.findElement(caesarSalad).click();
	}
	
	public List<String> addRequiredModifiers(){
		driver.findElement(By.xpath("//android.widget.TextView[@text='Large']/following-sibling::android.widget.TextView[2]")).click();
		List<String> listModifierPrice=new ArrayList<String>();
		listModifierPrice.add(driver.findElement(By.xpath("//android.widget.TextView[@text='Large']/following-sibling::android.widget.TextView[1]")).getText().substring(1));
		WebElement scroller=driver.findElements(By.id("rvModifiers")).get(1);
		scroll(scroller,1.0);
		driver.findElement(By.xpath("//android.widget.TextView[@text='0%']/following-sibling::android.widget.TextView[1]")).click();
		scroll(scroller,2.0);
		List<WebElement> listModifier=driver.findElements(addModifierBtn);
		listModifier.get(listModifier.size()-1).click();
		return listModifierPrice;
	}
	
	public void addSecondItem() {
		driver.findElements(foodItems).get(1).click();
	}
	
	public double addSecondModifier() {
		driver.findElement(By.xpath("//android.widget.TextView[@text='Large']/following-sibling::android.widget.TextView[2]")).click();
		return Double.parseDouble(driver.findElement(By.xpath("//android.widget.TextView[@text='Large']/following-sibling::android.widget.TextView[1]")).getText().substring(1));
	}
	
	public void addToCart() {
		waitForElementToBeClickable(addToCartBtn, 5).click();
	}
	
	public void addModifier() {
		driver.findElements(addModifierBtn).get(0).click();
	}
	
	public KioskCartPage viewCart() {
		waitForElementVisibility(viewCartBtn,5).click();
		return new KioskCartPage(driver);
	}
	
	public boolean isNotCart() {
		return driver.findElements(yourCart).size()==0;
	}
	
	public String getItemPrice() {
		String priceText = waitForElementVisibility(itemPrice, 10).getText(); 
	    return priceText.replace("$", ""); 
	}
	
	public String getItemPriceText() {
		String priceText = driver.findElement(itemPriceText).getText(); 
	    return priceText.replace("$", ""); 
	}
	
	public KioskCheckoutPage checkout() {
		driver.findElement(checkoutBtn).click();
		return new KioskCheckoutPage(driver);
	}
	
	public void scroll(WebElement ele,double percent) {
		((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", ImmutableMap.of(
			    "elementId", ((RemoteWebElement) ele).getId(),
			    "direction", "down",
			    "percent", percent
			));
	}
	
	public void swipe(WebElement ele,double percent) {
		((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", ImmutableMap.of(
			    "elementId", ((RemoteWebElement) ele).getId(),
			    "direction", "up",
			    "percent", percent
			));
	}	
	
	public void changeModifier() {
		driver.findElements(addModifierBtn).get(1).click();
	}
	
	public void remove() {
		driver.findElement(removeBtn).click();
	}
	
	public KioskCheckoutPage updateCart() {
		driver.findElement(updateCartBtn).click();
		return new KioskCheckoutPage(driver);
	}
	
	public List<String> getModifiersName(){
		List<WebElement> modifiers=driver.findElements(modifierName);
		List<String> listModifier=new ArrayList<String>();
		for(WebElement modifier:modifiers) {
			listModifier.add(modifier.getText());
		}
		return listModifier;
	}
	
	public double getItemTotalPrice() {
		return Double.parseDouble(driver.findElement(addToCartBtn).getText().substring(14).replace(")", ""));
	}
	
	public void customise() {
		driver.findElement(customiseBtn).click();
	}
	
	public void tapSpecialRequest() {
		driver.findElement(specialRequestBtn).click();
	}
	
	public void enterSpecialRequest(String request) {
		WebElement specialRequestField=driver.findElement(requestField);
		specialRequestField.click();
		specialRequestField.sendKeys(request);
	}
	
	public void startNew() {
		driver.findElement(startNewBtn).click();
		driver.findElement(yesBtn).click();
	}
	
	public void removeModifier() {
		driver.findElements(removeBtn).get(0).click();
	}
	
	public void waitForClearChanges() {
		waitForElementVisibility(clearChanges, 5);
	}
	
	public void clickSignatureFood() {
		driver.findElement(signatureFood).click();
	}
	
	public void clickSearch() {
		driver.findElement(search).click();
	}
	
	public void searchItem(String item) {
		WebElement searchBar = driver.findElement(searcBar);
		searchBar.sendKeys(item);
	}
	
	public List<String> getSearchedItems() {
		List<WebElement> searchedItems = driver.findElements(searchedItemList);
		List<String> searchedItemsList = new ArrayList<>();
		
		for(WebElement itemName : searchedItems)
		{
			searchedItemsList.add(itemName.getText());
		}
		
		return searchedItemsList;
	}
	
	public void closeSearchPopUp() {
		driver.findElement(closeSearch).click();
	}
	
	public int getCartButtonSize() {
		return driver.findElements(viewCartBtn).size();
	}
	
	public void waitForCustomizeButton() {
		waitForElementVisibility(customiseBtn, 10);
	}
	
	public void tapSaladAddOn() {
		waitForElementVisibility(saladAddOn, 5).click();
	}
	
	public void closeKeyboard() {
		driver.navigate().back();
	}
}
