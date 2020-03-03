package pom_Pages;

import org.openqa.selenium.By;

import com.ibm.icu.text.ChineseDateFormat.Field;

import baseClass.AppTest;
import utilities.elements.Element;
import utilities.parameters.ParameterTable;
import utilities.selenium.Log;

public class PurchaseConfirmationPage extends AppTest {
	// Locators
	public static By lblSuccessMsg = By.xpath("//div[contains(@class,'sweet-alert ')]//h2");
	public static By productInfo = By.xpath("//div[contains(@class,'sweet-alert ')]//p");
	public static By btnConfirm = By.xpath("//button[contains(@class,'confirm')]");

	// Methods
	public static void verifyProductIsPurchased() {
		Element.shouldHaveText(lblSuccessMsg, "Success Msg", "Thank you for your purchase!");
		String strProductInfo = Element.getText(productInfo, "ProductInfo");
		String[] arrProductInfo = strProductInfo.split("\n");
		String actualVal = (arrProductInfo[1].split(" "))[1];
		String expectedVal = ParameterTable.get("TotalPrice");
		if (actualVal.equals(expectedVal)) {
			Log.printPass("Purchased product's price matches with the expected price : " + actualVal);
		} else {
			Log.printFail("Purchased product's price : " + actualVal + " does not match with the expected price : " + expectedVal);
		}
	}

	public static void captureProductId() {
		System.out.println(Element.getText(productInfo, "ProductInfo"));
		String strProductInfo = Element.getText(productInfo, "ProductInfo");
		String[] arrProductInfo = strProductInfo.split("\n");
		String productID = arrProductInfo[0].split(" ")[1];
		String productPrice = "$" + (arrProductInfo[1].split(" "))[1];
		Log.printInfo("Purchased Product's ID: " + productID);
		Log.printInfo("Purchased Product's Price: " + productPrice);
		Log.printWorkflow("Purchased Product's ID: " + productID);
		Log.printWorkflow("Purchased Product's Price: " + productPrice);
	}

	public static void confirmPurchase() {
		Element.click(btnConfirm, "Confirm");
	}

}
