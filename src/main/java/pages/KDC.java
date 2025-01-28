package pages;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import basePage.BasePage;
import io.appium.java_client.android.AndroidDriver;

public class KDC extends BasePage {

    
    private By preparingOrder = By.xpath("//androidx.recyclerview.widget.RecyclerView[contains(@resource-id, 'rv_preparing')]");
    private By orderNumber = By.xpath(".//android.widget.TextView[contains(@resource-id, 'tv_order_number')]");
    private By readyOrder = By.xpath("//androidx.recyclerview.widget.RecyclerView[contains(@resource-id, 'rv_ready')]");
 
    

    public KDC(AndroidDriver driver) {
        super(driver);
    }

    public List<String> getPreparingOrderNumbers(String orderNum) {
        WebElement preparingOrders = driver.findElement(preparingOrder);
        List<WebElement> orderNumberElements = preparingOrders.findElements(orderNumber);
        List<String> orderNumbers = new ArrayList<>();

        for (WebElement orderElement : orderNumberElements) {
            String kdcOrderNumber = orderElement.getText();
            int length = kdcOrderNumber.length() - orderNum.length();
            orderNumbers.add(kdcOrderNumber.substring(length));
        }
        return orderNumbers;
    }
    
    public List<String> getReadyOrderNumbers(String orderNum) {
    	WebElement readyOrders = driver.findElement(readyOrder);
    	List<WebElement> orderNumberElements = readyOrders.findElements(orderNumber);
    	List<String> orderNumbers = new ArrayList<>();
    	
    	for(WebElement orderElement: orderNumberElements) {
    		String kdcOrderNumber = orderElement.getText();
    		int length = kdcOrderNumber.length() - orderNum.length();
    		orderNumbers.add(kdcOrderNumber.substring(length));
    	}
    	return orderNumbers;
    	
    }
}
