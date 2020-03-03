package utilities.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utilities.selenium.Driver;
import utilities.selenium.Log;

public class Element {

	public static void click(By by, String label) {
		WebElement element;
		Log.printAction(String.format("Clicking '%s'", label));
		element = Page.findElement(by, label);
		element.click();
	}

	public static void setText(By selector, String text) {
		Log.printAction(String.format("Entering text in '%s'", text));
		WebElement TextField;
		TextField = Page.findElement(selector, "'" + selector + "' text field");
		TextField = Driver.Instance.findElement(selector);
		TextField.sendKeys(text);
	}

	public static String getText(By by, String fieldName) {
		Log.printAction(String.format("Getting text from '%s' text field", fieldName));
		WebElement TextField;
		TextField = Page.findElement(by, fieldName);
		String text = TextField.getText();
		return text;
	}

	public static void shouldHaveText(By by, String elementName, String string) {
		String txt = getText(by, elementName);
		if (txt.equals(string)) {
			Log.printPass(String.format("The value of element '%s' matches with the expected value : '%s'", elementName, txt));
		} else {
			Log.printFail(String.format("Actual value '%s' did not match with the expected value '%s' for the element '%s'", txt, string, elementName));
		}
	}

}
