package pom_Pages;

import org.openqa.selenium.By;

import baseClass.AppTest;
import utilities.elements.Element;

public class PlaceOrderPage extends AppTest{
	//Locators
	public static By txtName = By.id("name");
	public static By txtCountry = By.id("country");
	public static By txtCity = By.id("city");
	public static By txtCard = By.id("card");
	public static By txtMonth = By.id("month");
	public static By txtYear = By.id("year");
	public static By lnkPurchaseOrder = By.xpath("//button[text()='Purchase']");
	
	public static void completeForm(String strName, String strCountry, String strCity, String strCreditCard, String strMonth, String strYear) {
		Element.setText(txtName, strName);
		Element.setText(txtCountry, strCountry);
		Element.setText(txtCity, strCity);
		Element.setText(txtCard, strCreditCard);
		Element.setText(txtMonth, strMonth);
		Element.setText(txtYear, strYear);
	}
	
	public static void purchaseOrder() {
		Element.click(lnkPurchaseOrder, "PurchaseOrder");
	}
}
