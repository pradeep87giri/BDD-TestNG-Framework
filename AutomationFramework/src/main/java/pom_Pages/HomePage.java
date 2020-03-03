package pom_Pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import baseClass.AppTest;
import utilities.elements.Element;
import utilities.elements.Page;
import utilities.selenium.Driver;
import utilities.selenium.Log;

public class HomePage extends AppTest {

	// Locators
	public static By lnkHome = By.xpath("//a[contains(text(),'Home')]");
	public static By lnkLaptops = By.xpath("//a[text()='Laptops']");
	public static By lnkCart = By.xpath("//a[text()='Cart']");
	public static By btnNext = By.id("next2");
	public static By btnPrevious = By.id("prev2");
	public static By tableLaptops = By.xpath("//div[@id='tbodyid']//a[@class='hrefch']");

	// Methods
	public static void navigateToLaptops() {
		Element.click(lnkHome, "Home");
		Element.click(lnkLaptops, "Laptops");
	}

	public static void clickOnProduct(String strProductName) throws InterruptedException{
		Driver.waitForPageToLoad();
		Thread.sleep(8000);
		boolean flag = false;
		List<WebElement> productList = Page.findElements(tableLaptops, "Laptop List");
		WebElement nxtButton = Page.findElement(btnNext, "NextButton");

		// Check if the product is present anywhere on the page
		while (flag != true) {
			for (WebElement product : productList) {
				if (product.getText().equals(strProductName)) {
					flag = true;
					product.click();
					break;
				}
			}
			// Search on next page if the product is not found
			if (flag == false) {
				if (nxtButton.isDisplayed() == true) {
					nxtButton.click();
				}
				// Fail if the product is not found on last page
				else {
					Log.printFail("This product is not present ");
					break;
				}
			}
		}
	}

	public static void navigateToCart() {
		Element.click(lnkCart, "Cart");
	}

}
