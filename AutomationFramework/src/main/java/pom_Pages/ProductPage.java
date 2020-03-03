package pom_Pages;

import org.openqa.selenium.By;

import baseClass.AppTest;
import utilities.elements.Element;
import utilities.parameters.ParameterTable;
import utilities.selenium.Driver;

public class ProductPage extends AppTest {
	//Locators
	public static By btnAddToCart = By.xpath("//a[text()='Add to cart']");
	public static By lblProductName = By.xpath("//h2");
	public static By lblProductPrice = By.xpath("//h3");
		
	// Methods
	public static void addToCart() {
		//Capturing product Price to verify later
		String productName = Element.getText(lblProductName, "Product Name");
		String productPrice = (Element.getText(lblProductPrice, "Product Price").split(" "))[0];
		ParameterTable.add(productName+" Price", productPrice);
		
		Element.click(btnAddToCart, "Add To Cart");
		Driver.acceptAlert();
	}
} 
