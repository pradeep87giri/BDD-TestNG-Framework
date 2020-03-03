package stepDefinitions;

import baseClass.AppTest;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pom_Pages.CartPage;
import pom_Pages.HomePage;
import pom_Pages.PlaceOrderPage;
import pom_Pages.ProductPage;
import pom_Pages.PurchaseConfirmationPage;
import utilities.selenium.Log;

public class DemoBlaze extends AppTest {

	@Given("^Open Browser$")
	public void openBrowser() throws Throwable {
		Log.printWorkflow("Open Browser");
		Log.printInfo("Opening Browser");
	}

	@When("^Add Laptops \"([^\"]*)\" to Cart$")
	public void addLaptops(String strProductsList) throws Throwable {
		Log.printWorkflow("Add Laptops");
		Log.printInfo("Add Laptops");
		String[] arrProducts = strProductsList.split(";");
		for (String strProductName : arrProducts) {
			HomePage.navigateToLaptops();
			Log.takeScreenshot();
			HomePage.clickOnProduct(strProductName);
			ProductPage.addToCart();
			Log.takeScreenshot();
		}
	}

	@And("^Delete Products \"([^\"]*)\" from Cart$")
	public void deleteProductFromCart(String strProductsList) throws Throwable {
		Log.printWorkflow("Delete Product from Cart");
		Log.printInfo("Delete Product from Cart");
		HomePage.navigateToCart();
		String[] arrProducts = strProductsList.split(";");
		for (String strProductName : arrProducts) {
			CartPage.deleteFromCart(strProductName);
			Log.takeScreenshot();
		}
	}

	@And("^Place Order$")
	public void placeOrder() throws Throwable {
		Log.printWorkflow("Place Order");
		Log.printInfo("Place Order");
		CartPage.placeOrder();
	}

	@And("^Complete Web Form with \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\"$")
	public void completeWebForm(String strName, String strCountry, String strCity, String strCreditCard,
			String strMonth, String strYear) throws Throwable {
		Log.printWorkflow("Complete Web Form");
		Log.printInfo("Complete Web Form");
		PlaceOrderPage.completeForm(strName, strCountry, strCity, strCreditCard, strMonth, strYear);
		Log.takeScreenshot();
	}

	@And("^Purchase Order$")
	public void PurchaseOrder() throws Throwable {
		Log.printWorkflow("Purchase Order");
		Log.printInfo("Purchase order");
		PlaceOrderPage.purchaseOrder();
	}

	@And("^Capture Product Info$")
	public void captureProductInfo() {
		Log.printWorkflow("Capture Product Info");
		Log.printInfo("Capture Product Info");
		PurchaseConfirmationPage.captureProductId();
		Log.takeScreenshot();
	}

	@Then("^Verify Product is Purchased$")
	public void verifyProductIsPurchased() throws Throwable {
		Log.printWorkflow("Verify Product is Purchased");
		Log.printInfo("Verify Product is Purchased");
		PurchaseConfirmationPage.verifyProductIsPurchased();
	}

	@And("^Click OK to Confirm Purchase$")
	public void ClickOK() throws Throwable {
		Log.printWorkflow("Click OK");
		Log.printInfo("Click OK");
		PurchaseConfirmationPage.confirmPurchase();
	}

}
