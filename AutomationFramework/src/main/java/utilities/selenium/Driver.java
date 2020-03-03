package utilities.selenium;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * The Driver class is a wrapper for Selenium's WebDriver class. It provides an
 * instance of a Selenium WebDriver, which is chosen and initialized at the
 * start of a test based on runtime parameters.
 */

public class Driver {
	public static WebDriver Instance;
	public static String baseAddress = ExcelDataConfig.getProperty("testURL");
	public static String mainWindowHandle = null;
	public static int timeOut = 60;
	public static final String USERNAME = "";
	public static final String AUTOMATE_KEY = "";
	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

	// Initializes the driver and opens the specified browser (Firefox, Chrome, or
	// Internet Explorer).
	public static void initialize(String browser, String browserDriver) throws Exception {
		if (browser.contentEquals("firefox")) {
			Instance = new FirefoxDriver();
			DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
			desiredCapabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
					UnexpectedAlertBehaviour.DISMISS);
			FirefoxProfile firefoxProfile = new FirefoxProfile();
			firefoxProfile.setPreference("browser.private.browsing.autostart", true);
		} else if (browser.contentEquals("chrome")) {
			String chromeDriverLinuxPath = System.getProperty("user.dir") + "//Library//webdrivers//chromedriver";
			ChromeOptions chromeOptions = new ChromeOptions();
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();

			chromePrefs.put("incognito.mode_availability", 2);
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.prompt_for_download", "false");
			chromePrefs.put("safebrowsing.enabled", "true");

			chromeOptions.setExperimentalOption("prefs", chromePrefs);
			chromeOptions.addArguments("--start-maximized");
			chromeOptions.addArguments("--disable-extensions", "--disable-infobars", "--incognito",
					"--safebrowsing-disable-download-protection", "--no-sandbox");
			chromeOptions.setCapability("chrome.switches", Arrays.asList("--incognito"));
			chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

			if (SystemUtils.IS_OS_WINDOWS) {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + browserDriver);
			} else if (SystemUtils.IS_OS_LINUX) {
				File f = new File(chromeDriverLinuxPath);
				f.setExecutable(true);
				System.setProperty("webdriver.chrome.driver", chromeDriverLinuxPath);

			} else
				throw new Exception("Unsupported Operating System : " + SystemUtils.OS_NAME);

			Instance = new ChromeDriver(chromeOptions);
		} else if (browser.contentEquals("ie")) {
			System.setProperty("webdriver.ie.driver",
					System.getProperty("user.dir") + "\\Library\\webdrivers\\IEDriverServer.exe");
			Instance = new InternetExplorerDriver();

			DesiredCapabilities desiredCapabilities = DesiredCapabilities.internetExplorer();
			desiredCapabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
					UnexpectedAlertBehaviour.DISMISS);

			// Open browser in private mode
			desiredCapabilities.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
		}

		Instance.manage().window().maximize();
		// Get window handle for future use
		mainWindowHandle = Driver.Instance.getWindowHandle();
	}

	// Closes the current driver.
	public static void close() {
		Instance.quit();
	}

	public static void waitUntilWebElementExists(By selector) {
		WebDriverWait wait = new WebDriverWait(Driver.Instance, 60);
		wait.until(ExpectedConditions.presenceOfElementLocated(selector)).isDisplayed();
	}

	public static void waitForPageToLoad() {
		try {
			while (true) // Handle the timeout
			{
				Driver.pause(1);
				String pageLoad = (String) ((JavascriptExecutor) Driver.Instance).executeScript("return document.readyState");
				if (pageLoad.equals("complete"))
					break;
				Driver.pause(.5);
			}
		} catch (WebDriverException e) {
			System.out.println("InsideL");
			Driver.pause(3);
		}
	}

	public static void waitForAlert() {
		int i = 0;
		while (i++ < 5) {
			try {
				Alert alert = Driver.Instance.switchTo().alert();
				break;
			} catch (NoAlertPresentException e) {
				Driver.pause(3);
				continue;
			}
		}
	}

	public static void acceptAlert() {
		waitForAlert();
		Driver.Instance.switchTo().alert().accept();
	}

	// Pause the test for a specified amount of seconds
	public static void pause(double seconds) {
		try {
			Thread.sleep((long) (seconds * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
