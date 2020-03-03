package utilities.elements;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import utilities.selenium.Driver;
import utilities.selenium.Log;

public class Page {

	public static WebElement findElement(By by, String elementName) {
		WebElement we = null;
		try {
			we = Driver.Instance.findElement(by);
			return we;
		} catch (Exception ex1) {
			Assert.fail(String.format("Element '%s' not found", elementName));
			return null;
		}
	}

	public static List<WebElement> findElements(By by, String elementName) {
		List<WebElement> we;
		try {
			we = Driver.Instance.findElements(by);
			return we;
		} catch (Exception e) {
			Assert.fail(String.format("Element '%s' not found", elementName));
			return null;
		}
	}

	public static void verifyElementPresent(By by, String element) {
		if (!Driver.Instance.findElements(by).isEmpty()) {
			Log.printPass(String.format("'%s' is present on Page", element));
		} else {
			Log.printFail(String.format("'%s' is not present on Page", element));
		}
	}

	public static void verifyElementNotPresent(By by, String element) {
		if (Driver.Instance.findElements(by).isEmpty()) {
			Log.printPass(String.format("'%s' is not present on Page", element));
		} else {
			Log.printFail(String.format("'%s' is present on Page", element));
		}
	}

}
