package utilities.selenium;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilities.elements.Page;

/**
 * The Driver class is a wrapper for Selenium's WebDriver class. It provides an instance of a
 * Selenium WebDriver, which is chosen and initialized at the start of a test based on runtime
 * parameters. Methods for pausing the driver are also provided.
 */
public class Driver 
{
	public static WebDriver Instance;
	public static String baseAddress = ExcelDataConfig.getProperty("testURL");
	
	public static String mainWindowHandle = null;
	public static int timeOut = 60;
	public static final String USERNAME = "";
	public static final String AUTOMATE_KEY = "";
	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

	/**
	 * Initializes the driver and opens the specified browser (Firefox, Chrome, or Internet
	 * Explorer). The window size is also changed after opening the browser.
	 * 
	 * @param browser String of the specified browser
	 * @throws Exception 
	 */
	public static void initialize(String browser,String browserDriver) throws Exception
	{
		if (browser.contentEquals("firefox"))
		{
			Instance = new FirefoxDriver();

			DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
			desiredCapabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);

			FirefoxProfile firefoxProfile = new FirefoxProfile();
			firefoxProfile.setPreference("browser.private.browsing.autostart", true);
		}
		else if (browser.contentEquals("chrome"))
		{
			String chromeDriverLinuxPath = System.getProperty("user.dir") + "//Library//webdrivers//chromedriver";
			 
	          //System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//webdrivers//chromedriver");
	             //Instance = new ChromeDriver();
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
//	             chromeOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
//						UnexpectedAlertBehaviour.ACCEPT_AND_NOTIFY);
	             chromeOptions.setCapability("chrome.switches", Arrays.asList("--incognito"));
	             chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
	             
	             //To use user data directory
//	             chromePrefs.put("download.default_directory", SystemUtils.getJavaIoTmpDir().getAbsolutePath());
//	             chromeOptions.addArguments("user-data-dir=" + System.getProperty("user.home") + "/AppData/Local/Google/Chrome/User Data");
	             
	             if (SystemUtils.IS_OS_WINDOWS) {
	            	 System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + browserDriver);
	             }
	             else if (SystemUtils.IS_OS_LINUX) {
	                 File f = new File(chromeDriverLinuxPath);
	                 f.setExecutable(true);
	                 System.setProperty("webdriver.chrome.driver", chromeDriverLinuxPath);
	     			
	             } else
	                 throw new Exception("Unsupported Operating System : " + SystemUtils.OS_NAME);
	            
	        	Instance = new ChromeDriver(chromeOptions);
		}
		else if (browser.contentEquals("ie"))
		{
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\Library\\webdrivers\\IEDriverServer.exe");
			Instance = new InternetExplorerDriver();

			DesiredCapabilities desiredCapabilities = DesiredCapabilities.internetExplorer();
			desiredCapabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);

			// Open browser in private mode
			desiredCapabilities.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
		}
		// turnOnWait();
		
		else if(browser.contentEquals("browserstack")){

			
			  
		  	DesiredCapabilities caps = new DesiredCapabilities();

		  	caps.setJavascriptEnabled(false);
		  	caps.setCapability("browser", "Chrome");
		  	caps.setCapability("browser_version", "53.0");
		  	caps.setCapability("browserstack.local", "true");
		  	caps.setCapability("os", "Windows");
		  	caps.setCapability("os_version", "7");
		  	caps.setCapability("resolution", "1920x1080");
		    Instance = new RemoteWebDriver(new URL(URL), caps);
	}

	// Resize browser window
	//Instance.manage().window().setSize(new Dimension(1400, Instance.manage().window().getSize().height));
	Instance.manage().window().maximize();
	// Get window handle for future use
	mainWindowHandle = Driver.Instance.getWindowHandle();
}

		// Resize browser window
		//Instance.manage().window().setSize(new Dimension(1400, Instance.manage().window().getSize().height));
	
	/**
	 *  Switch to other window
	 */
	
	public static void switchToOtherWindow(){
		Set<String> windows = Driver.Instance.getWindowHandles();
		while (windows.size() < 2) {

			windows = Driver.Instance.getWindowHandles();
		}

		windows.remove(Driver.mainWindowHandle);
		
		for (String window : windows) {
		
			Driver.Instance.switchTo().window(window);

		}

	}

	/**
	 * Closes the current driver.
	 */
	public static void close()
	{
		
		Instance.quit();
	}

	/**
	 * TODO: Javadocs
	 * 
	 * @param action
	 */
	public static void noWait(Runnable action)
	{
		turnOffWait();
		action.run();
		turnOnWait();
	}

	/**
	 * TODO: Javadocs
	 */
	private static void turnOffWait()
	{
		Instance.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}

	/**
	 * TODO: Javadocs
	 */
	private static void turnOnWait()
	{
		Instance.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}
	
	/**
	 * Create a org.openqa.selenium.support.ui.FluentWait instance with
	 * timeout/polling.
	 *
	 * Sample usage: {@code
	 *   Wait().until(invisibilityOfElementLocated(By.id("magic-id")));
	 * }
	 *
	 * @return instance of org.openqa.selenium.support.ui.FluentWait
	 */
	public static FluentWait<WebDriver> Wait() {
		return new FluentWait<>(Driver.Instance).withTimeout(Duration.ofSeconds(Driver.timeOut))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
	}
	
	
	/**
	 * Test will pause until the specified element is displayed on the page.
	 * 
	 * @param selector Selenium selector. Ex.) By.id("")
	 */
	
	public static boolean isElementPresent(By by){
        try{
            if (!Driver.Instance.findElements(by).isEmpty())  return true;
            else  return false;
        }
        catch(NoSuchElementException e){
            return false;
        }
    }

	/**
	 * Test will pause until the specified element is displayed on the page.
	 * 
	 * @param selector Selenium selector. Ex.) By.id("")
	 */
	public static void waitUntilWebElementExists(By selector)
	{
		WebDriverWait wait = new WebDriverWait(Driver.Instance, 60);
		wait.until(ExpectedConditions.presenceOfElementLocated(selector)).isDisplayed();
	}

	/**
	 * Force the browser to wait for all Java script calls to complete after the page has fully
	 * loaded.
	 */
	public static void waitForAjaxToComplete()
	{
		try
		{
			while (true) // Handle the timeout
			{
				Driver.pause(1);
				
				boolean ajaxIsComplete = (boolean) ((JavascriptExecutor) Driver.Instance).executeScript("return jQuery.active == 0");
				if (ajaxIsComplete)
					break;
				// Log.printInfo("ajax is still running");  // for debugging purposes
				Driver.pause(.5);
				
			}
		}
		catch (WebDriverException e)
		{
			Driver.pause(3);}
	}
	
	public static void waitForPageToLoad()
	{
		try
		{
			while (true) // Handle the timeout
			{
				Driver.pause(1);
				//System.out.println("OutsideL");
				String pageLoad = (String) ((JavascriptExecutor) Driver.Instance).executeScript("return document.readyState");
				//System.out.println(pageLoad);
				if (pageLoad.equals("complete"))
					break;
				// Log.printInfo("ajax is still running");  // for debugging purposes
				Driver.pause(.5);
				//System.out.println("OutsideL1");
			}
		}
		catch (WebDriverException e)
		{System.out.println("InsideL");
			Driver.pause(3);}
	}
	
	public static void waitForAjaxToCompleteWithWait()
	{
		try
		{
			while (true) // Handle the timeout
			{
				
				boolean ajaxIsComplete = (boolean) ((JavascriptExecutor) Driver.Instance).executeScript("return jQuery.active == 0");
				if (ajaxIsComplete)
					break;
				// Log.printInfo("ajax is still running");  // for debugging purposes
				Driver.pause(.5);
			}
		}
		catch (WebDriverException e)
		{
			Driver.pause(3);
		}
	}
	
	/*public static void waitForDialogToComplete()
	{
		try
		{
			while (true) // Handle the timeout
			{
				Driver.pause(1);
				boolean complete = Driver.isElementPresent(By.cssSelector("div[class*='apc apc-dialog dialog-modal']"));
				if (!complete)
					break;
			}
		}
		catch (WebDriverException e)
		{
			Driver.pause(3);
		}
	}*/
	
	
	public static void waitForAlert()
	{
		int i=0;
		while(i++<5)
		{
	        try
	        {
	            Alert alert = Driver.Instance.switchTo().alert();
	            break;
	        }
	        catch(NoAlertPresentException e)
	        {
	        	Driver.pause(3);
	        	continue;
	        }
	   }
	}
	
	public static void acceptAlert(){
		waitForAlert();
		Driver.Instance.switchTo().alert().accept();
	}

	/**
	 * Pause the test for a specified amount of seconds
	 * 
	 * @param seconds Double amount of seconds. Can use decimal if need be
	 */
	public static void pause(double seconds)
	{
		try
		{
			Thread.sleep((long) (seconds * 1000));
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	

	/**
	 * @return A String of the current date and time in the format of 'yyyyMMddHHmmss'.
	 */
	public static String getDateTimeStamp()
	{
		return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
	}
	
	/**
	 *  Switch to other frame
	 */
	
	public static void switchToFrame(By frame,String frameName){
		WebElement desiredFrame;
		Driver.Instance.switchTo().defaultContent();
		desiredFrame=Page.findElement(frame, frameName);
		Driver.Instance.switchTo().frame(desiredFrame);
	}
	
	public static void switchToDefaultFrame(){
		Driver.Instance.switchTo().defaultContent();
	}
	
	public static void waitForLoadingtoComplete(){
	
		Log.printInfo("Waiting for the Loading to Complete");
		while(Page.findElement(By.xpath("//p[@class='Loading...']"), "loading label").isDisplayed()){
			Driver.pause(5);
			
		}		
	}
	public static void scrollDown(WebElement element){
		
		JavascriptExecutor js = (JavascriptExecutor) Instance;
		js.executeScript("arguments[0].scrollIntoView();", element);
	}
	
	public static void scrollup() {
		JavascriptExecutor js = ((JavascriptExecutor) Instance);
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}	
	
	public static void moveToElement(WebElement ele) {
	Actions action = new Actions(Instance);
	action.moveToElement(ele).perform();
	}
	
	
	
}
