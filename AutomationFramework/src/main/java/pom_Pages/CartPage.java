package pom_Pages;

import org.openqa.selenium.By;
import baseClass.AppTest;
import utilities.elements.Element;
import utilities.elements.Page;
import utilities.parameters.ParameterTable;
import utilities.selenium.Driver;

public class CartPage extends AppTest{
	
	//Locators
	public static By lnkPlaceOrder = By.xpath("//button[text()='Place Order']");
	public static By lblPrice = By.id("totalp");
			
	//Methods
	public static void deleteFromCart(String strProductName) {
		By btnDelete = By.xpath("//td[text()='"+strProductName+"']/parent::tr//td/a[text()='Delete']");
		Element.click(btnDelete, "Delete");
		Driver.waitForPageToLoad();
		//Check if the product is deleted successfully
		Page.verifyElementNotPresent(btnDelete, "Product "+ strProductName);
	}
	
	public static void placeOrder() {
		//Capturing Total price for verification
		String strTotalPrice = Element.getText(lblPrice, "TotalPrice");
		ParameterTable.add("TotalPrice", strTotalPrice);
		Element.click(lnkPlaceOrder, "Place Order");
	}
	
	
	
}
