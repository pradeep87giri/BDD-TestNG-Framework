
package utilities.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Optional;

import com.relevantcodes.extentreports.LogStatus;

/**
 * A library with functions to print out a log. This will print a log both in ReportNG and to the
 * console.
 */
public class Log 
{
	private static final List<String> workflowSteps =new ArrayList<String>();
	
	public static void takeScreenshot()
	{
//		printWorkflow("Capturing Screenshot");
				
		File screenshot = ((TakesScreenshot)Driver.Instance).getScreenshotAs(OutputType.FILE);
		String screenshotDirectory = System.getProperty("user.dir") + "\\test-output\\ExtentReport\\";
		String fileName = "./"+SeleniumBaseTest.currentTest + "_" + System.currentTimeMillis() + ".jpg";
		File targetFile = new File(screenshotDirectory, fileName);
		try
		{
			FileUtils.copyFile(screenshot, targetFile);
			Driver.pause(2);
			InputStream is = new FileInputStream(targetFile);
			byte[] imageBytes = IOUtils.toByteArray(is);
			Driver.pause(2);
			String base64 = Base64.getEncoder().encodeToString(imageBytes);
			
//			SeleniumBaseTest.parent.log(LogStatus.INFO, "Screenshot:"
//					+ SeleniumBaseTest.parent.addBase64ScreenShot("data:image/png;base64,"+base64));
			
			SeleniumBaseTest.child.log(LogStatus.INFO, "Screenshot:"
					+ SeleniumBaseTest.child.addBase64ScreenShot("data:image/png;base64,"+base64));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void takeScreenshotOnFailure()
	{
//		printWorkflow("Capturing Screenshot");
				
		File screenshot = ((TakesScreenshot)Driver.Instance).getScreenshotAs(OutputType.FILE);
		String screenshotDirectory = System.getProperty("user.dir") + "\\test-output\\ExtentReport\\";
		String fileName = "./"+SeleniumBaseTest.currentTest + "_" + System.currentTimeMillis() + ".jpg";
		File targetFile = new File(screenshotDirectory, fileName);
		try
		{
			FileUtils.copyFile(screenshot, targetFile);
			Driver.pause(2);
			InputStream is = new FileInputStream(targetFile);
			byte[] imageBytes = IOUtils.toByteArray(is);
			Driver.pause(2);
			String base64 = Base64.getEncoder().encodeToString(imageBytes);
			
			SeleniumBaseTest.parent.log(LogStatus.INFO, "Screenshot:"
					+ SeleniumBaseTest.parent.addBase64ScreenShot("data:image/png;base64,"+base64));
			
			SeleniumBaseTest.child.log(LogStatus.INFO, "Screenshot:"
					+ SeleniumBaseTest.child.addBase64ScreenShot("data:image/png;base64,"+base64));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Print action of what's happening followed by "..."
	 * 
	 * @param info The string you want to print. Ex). Clicking 'Save' Button
	 */
	public static void printAction(String info)
	{
		Reporter.log(info + "...", true);

		SeleniumBaseTest.child.log(LogStatus.INFO, info + "...");
	}
	
	/**
	 * Print a FAIL message to the log followed by "...", capture a screen shot,
	 * and set the test status to FAILURE.
	 * 
	 * @param message The string you want to print. Ex). Clicking 'Save' Button
	 */
	public static void printFail(String message)
	{
		Reporter.log("FAIL: " + message + "...", true);
		
		SeleniumBaseTest.child.log(LogStatus.FAIL, "<b><font color='red'>" + message + "...</font></b>");
	}
	
	/**
	 * Print a PASS message to the log followed by "..."
	 * 
	 * @param message The string you want to print. Ex). Clicking 'Save' Button
	 */
	public static void printPass(String message)
	{
		Reporter.log("PASS: " + message + "...", true);
		
		SeleniumBaseTest.child.log(LogStatus.PASS, message + "...");
	}

	/**
	 * Print an ERROR message to the log followed by "...", capture a screen shot,
	 * and set the test status to FAILURE.
	 * 
	 * @param message The string you want to print. Ex). Clicking 'Save' Button
	 */
	public static void printError(String message)
	{
		Reporter.log("ERROR: " + message + "...", true);
		
		SeleniumBaseTest.child.log(LogStatus.ERROR, "<b><font color='orangered'>" + message + "...</font></b>");
	}
	
	
	/**
	 * Print a message to show the test is going to retry.
	 */
	public static void printTestRetry()
	{
		Reporter.log("\n"
			+ "Test failure was detected, retrying the test...\n\n\n", true);

		SeleniumBaseTest.child.log(LogStatus.SKIP, "<b><font color='red'>Test failure was detected, retrying the test...</font></b>");
	}
	
	/**
	 * Print a message to show the test failed.
	 */
	public static void printTestFail()
	{
		Reporter.log("\n"
			+ "Test failed...\n", true);

		SeleniumBaseTest.child.log(LogStatus.FAIL, "<b><font color='red'>Test failed...</font></b>");
	}
	
	/**
	 * Print a message to show the test passed.
	 */
	public static void printTestPass()
	{
		Reporter.log("\n"
			+ "Test passed...\n", true);

		SeleniumBaseTest.child.log(LogStatus.PASS, "Test passed...");
	}
	
	
	/**
	 * Print a FATAL message to the log followed by "...", capture a screen shot,
	 * and set the test status to FAILURE.
	 * 
	 * @param message The string you want to print. Ex). Clicking 'Save' Button
	 */
	public static void printFatal(String message)
	{
		Reporter.log("FATAL: " + message + "...", true);
		
		SeleniumBaseTest.child.log(LogStatus.FATAL, "<b><font color='maroon'>" + message + "...</font></b>");
	}
	
	/**
	 * Print an UNKNOWN message to the log followed by "..."
	 * 
	 * @param message The string you want to print. Ex). Clicking 'Save' Button
	 */
	public static void printUnknown(String message)
	{
		Reporter.log("UNKNOWN: " + message + "...", true);
		
		SeleniumBaseTest.child.log(LogStatus.UNKNOWN, message + "...");
	}
	
	/**
	 * Print a SKIP message to the log followed by "...", capture a screen shot,
	 * and set the test status to SKIP.
	 * 
	 * @param message The string you want to print. Ex). Clicking 'Save' Button
	 */
	public static void printSkip(String message)
	{
		Reporter.log("SKIP: " + message + "...", true);

		SeleniumBaseTest.child.log(LogStatus.SKIP, message + "...");
	}
	
	
	
	/**
	 * Print a warning in the test follower by "..."
	 * 
	 * @param info The string you want to print. Ex). Clicking 'Save' Button
	 */
	public static void printWarning(String info)
	{
		Reporter.log(info + "...", true);
		
		SeleniumBaseTest.child.log(LogStatus.WARNING, info + "...");
	}

	/**
	 * This will print a workflow statement which is different than an action. This is the step of
	 * what's happening in the test. Ex.) Creating a new account.
	 * 
	 * @param info The string you want to print in between 2 "#"
	 */
	public static void printWorkflow(String info)
	{
		Reporter.log("\n## " + info + " ##", true);
		workflowSteps.add(info);
		SeleniumBaseTest.child.log(LogStatus.INFO, "");
		SeleniumBaseTest.child.log(LogStatus.INFO, "\n## " + info + " ##");
	}
	
	
	public static void clearWorkflowSteps() {
	
		workflowSteps.clear();
	}
	
	public static String getWorkflowSteps() {
		return workflowSteps.stream().collect(Collectors.joining("<br />"));
	}

	
	/**
	 * Print an INFO message to the log followed by "..."
	 * 
	 * @param message The string you want to print. Ex). Clicking 'Save' Button
	 */
	public static void printInfo(String message)
	{
		Reporter.log(message + "...", true);

		SeleniumBaseTest.child.log(LogStatus.INFO, message + "...");
	}
	
	
	/**
	 * Print a message to show the test has failed for a test status of SKIP.
	 */
	public static void printSkipException(ITestResult result)
	{
		if (result.getThrowable() != null)
		{
			Reporter.log("\n"
				+ "FAILURE EXCEPTION:  " + result.getThrowable(), true);
			
			SeleniumBaseTest.child.log(LogStatus.SKIP, "<b><font color='red'>FAILURE EXCEPTION:</font></b>");
			SeleniumBaseTest.child.log(LogStatus.SKIP, result.getThrowable());
			
			takeScreenshot();
		}
	}
	
	/**
	 * Print a message to show the test has failed for a test status of FAILURE.
	 */
	public static void printFailureException(ITestResult result)
	{
		if (result.getThrowable() != null)
		{
			Reporter.log("\n"
				+ "FAILURE EXCEPTION:  " + result.getThrowable(), true);
			
			SeleniumBaseTest.child.log(LogStatus.FAIL, "<b><font color='red'>FAILURE EXCEPTION:</font></b>");
			SeleniumBaseTest.child.log(LogStatus.FAIL, result.getThrowable());
			
			takeScreenshot();
		}
	}
	public static void printTestStart()
	{
		Reporter.log("", true);
		Reporter.log("+-------------------------------------------------------------+", true);
		
		Reporter.log("\nINITIATING TEST: " + SeleniumBaseTest.currentClass + " | USER: " + SeleniumBaseTest.currentUser + "\n" + "BROWSER: " + SeleniumBaseTest.currentBrowser + "\n" + "URL: "
			+ SeleniumBaseTest.url + "\n", true);
		Reporter.log("+-------------------------------------------------------------+", true);

		SeleniumBaseTest.child.log(LogStatus.INFO, "");
		SeleniumBaseTest.child.log(LogStatus.INFO, "+-------------------------------------------------------------+");
		SeleniumBaseTest.child.log(LogStatus.INFO, "\nINITIATING TEST: " + SeleniumBaseTest.currentTest + " | USER: " +SeleniumBaseTest.currentUser  + "\n" + "BROWSER: " + SeleniumBaseTest.currentBrowser + "\n"
			+ "URL: " + SeleniumBaseTest.url + "\n");
		SeleniumBaseTest.child.log(LogStatus.INFO, "+-------------------------------------------------------------+");
	}

	
	public static void printScenarioStart()
	{
		Reporter.log("", true);
		Reporter.log("+-------------------------------------------------------------+", true);
		
		Reporter.log("\nINITIATING SCENARIO: " + SeleniumBaseTest.scenarioName + " : " + " | USER: " + SeleniumBaseTest.currentUser + "\n" + "BROWSER: " + SeleniumBaseTest.currentBrowser + "\n" + "URL: "
			+ SeleniumBaseTest.url + "\n", true);
		Reporter.log("+-------------------------------------------------------------+", true);

		SeleniumBaseTest.child.log(LogStatus.INFO, "");
		SeleniumBaseTest.child.log(LogStatus.INFO, "+-------------------------------------------------------------+");
		SeleniumBaseTest.child.log(LogStatus.INFO, "\nINITIATING SCENARIO: " + SeleniumBaseTest.scenarioName + " | USER: " +SeleniumBaseTest.currentUser  + "\n" + "BROWSER: " + SeleniumBaseTest.currentBrowser + "\n"
			+ "URL: " + SeleniumBaseTest.url + "\n");
		SeleniumBaseTest.child.log(LogStatus.INFO, "+-------------------------------------------------------------+");
	}
	
	
	public static void printFeatureStart()
	{
		Reporter.log("", true);
		Reporter.log("+-------------------------------------------------------------+", true);
		
		Reporter.log("\nINITIATING FEATURE FILE: " + SeleniumBaseTest.currentClass  + "\n" + "BROWSER: " + SeleniumBaseTest.currentBrowser + "\n", true);
		Reporter.log("+-------------------------------------------------------------+", true);

		
	}
	
	
	public static void printTestEnd()
	{
		Reporter.log("CLOSING TEST: " + SeleniumBaseTest.currentClass, true);
		Reporter.log("+^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^+\n", true);

		SeleniumBaseTest.child.log(LogStatus.INFO, "");
		SeleniumBaseTest.child.log(LogStatus.INFO, "\nCLOSING TEST: " + SeleniumBaseTest.currentTest);
		SeleniumBaseTest.child.log(LogStatus.INFO, "\n+^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^+\n");
	}

	
	public static void printScenarioEnd()
	{
		Reporter.log("CLOSING SCENARIO: " + SeleniumBaseTest.scenarioName, true);
		Reporter.log("+^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^+\n", true);

		SeleniumBaseTest.child.log(LogStatus.INFO, "");
		SeleniumBaseTest.child.log(LogStatus.INFO, "\nCLOSING SCENARIO: " + SeleniumBaseTest.scenarioName);
		SeleniumBaseTest.child.log(LogStatus.INFO, "\n+^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^+\n");
	}
	
	public static void printTestRestart(ITestResult result)
	{
		Reporter.log("***** FAILURE DETECTED: Retrying " + result.getThrowable(), true);

		SeleniumBaseTest.child.log(LogStatus.INFO, "");
		SeleniumBaseTest.child.log(LogStatus.INFO, "***** FAILURE DETECTED: Retrying " + result.getThrowable());
	}

	/**
	 * Used for debugging for better visibility
	 * 
	 * @param message (Optional) The string you want to print.
	 */
	public static void debug(@Optional String message)
	{
		if (message == null)
			message = "";

		System.out.println();
		System.out.println("*****************************************************************");
		System.out.println("DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG\n");
		System.out.println(message + "\n");
		System.out.println("DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG");
		System.out.println("*****************************************************************");
		System.out.println();
	}

	/**
	 * Used for debugging for better visibility
	 * 
	 * @param messages (Optional) The strings you want to print.
	 */
	public static void debug(@Optional String[] messages)
	{
		if (messages == null)
			messages = new String[] {" "};

		System.out.println();
		System.out.println("*****************************************************************");
		System.out.println("DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG\n");

		for (int i = 0; i < messages.length; i++)
			System.out.println(messages[i] + "\n");

		System.out.println("DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG-DEBUG");
		System.out.println("*****************************************************************");
		System.out.println();
	}

	
	public static void printTestData()
	{
		Reporter.log("\n+-------------------------------------------------------------+\n", true);
		Reporter.log("## Printing Test Data ##", true);
		Reporter.log("\n+-------------------------------------------------------------+\n", true);
	}
}
