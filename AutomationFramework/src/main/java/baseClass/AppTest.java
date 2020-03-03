package baseClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.TestNGCucumberRunner;
import utilities.extentreport.ExtentManager;
import utilities.selenium.Driver;
import utilities.selenium.ExcelDataConfig;
import utilities.selenium.Log;
import utilities.selenium.SeleniumBaseTest;

/**
 * SeleniumBaseTest must be extended by all the classes It acts as a base class
 * for other classes.
 */
public class AppTest extends SeleniumBaseTest implements IRetryAnalyzer {
	private int retryCount = 0;
	public int flag = 0;
	protected static int maxRetryCount = 1;
	private static String defaultBrowser = "chrome";
	public static String browser;
	public static String environment;
	public static String executionStage = "Local";
	public static List<String> passedtests = new ArrayList<String>();
	public static List<String> failedtests = new ArrayList<String>();
	public static List<String> skippedtests = new ArrayList<String>();
	public static List<ITestNGMethod> passedtest = new ArrayList<ITestNGMethod>();
	public static List<ITestNGMethod> failedtest = new ArrayList<ITestNGMethod>();
	public static List<ITestNGMethod> skippedtest = new ArrayList<ITestNGMethod>();
	public static List<String> tests = new ArrayList<String>();
	private static TestNGCucumberRunner testNGCucumberRunner;
	public static String sheetPath;
	public static String root = System.getProperty("user.dir");
	public static HashMap<String, String> data = new HashMap<String, String>();

	// Hashmap for selection as per role
	@Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "features")
	public void feature(CucumberFeatureWrapper cucumberFeature) {
		testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
	}

	// Initializing Environment variables
	public static void initializeEnvironment() throws Exception {
		if (executionStage.equals("Local")) {
			environment = ExcelDataConfig.getParameterValue(sheetPath, "EnvironmentDetails", "environment");
		} else if (executionStage.equals("External")) {
			environment = System.getProperty("environment");
		}

	}

	public static String getCurrentTest() {
		return SeleniumBaseTest.currentTest;

	}

	public static String getCurrentBrowser() {
		return SeleniumBaseTest.currentBrowser;
	}

	public static String getCurrentUser() {
		return SeleniumBaseTest.currentUser;
	}

	public static String getCurrentPassword() {
		return SeleniumBaseTest.currentPassword;
	}

	// If a test fails, this method will automatically run it again up to a
	// specified number of times.
	public boolean retry(ITestResult result) {
		if (retryCount < maxRetryCount) {
			Log.printTestRestart(result);
			retryCount++;

			return true;
		}
		return false;
	}

	// Begins ExtentReports and creates the parentTest for the report.
	@Parameters({ "dataFile", "sheet" })
	public static void createParentNode(ITestContext testName, @Optional String dataFile, @Optional String sheet) {
		try {
			ExcelDataConfig.initialize(dataFile, sheet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		suiteName = testName.getCurrentXmlTest().getSuite().getName();
		SeleniumBaseTest.extent = ExtentManager
				.getReporter("test-output/ExtentReport/" + suiteName + "_TestReport.html");
	}

	// Set Log4j setting to get rid of warning messages that show in the console.
	@BeforeSuite
	protected static void suppressLog4jWarnings() throws Throwable {
		Logger.getRootLogger().setLevel(Level.OFF);
		sheetPath = root + "\\src\\test\\resources\\DataSheet.xlsx";
	}

	@BeforeClass
	public void start(ITestContext context) throws Exception {
		String className = context.getCurrentXmlTest().getSuite().getClass().getName();
		SeleniumBaseTest.currentClass = this.getClass().getSimpleName();

		System.out.println("Class Name : " + SeleniumBaseTest.currentClass);

		File file = new File(
				root + "\\src\\test\\resources\\ParameterFiles\\" + SeleniumBaseTest.currentClass + "_parameters.txt");
		sheetPath = root + "\\src\\test\\resources\\DataSheet.xlsx";
		String[] cmd = { "cmd.exe", "/c",
				"del \"" + root + "\\src\\test\\resources\\ParameterFiles\\" + SeleniumBaseTest.currentClass
						+ "_parameters.txt\" " + "& copy /y NUL \"" + root + "\\src\\test\\resources\\ParameterFiles\\"
						+ SeleniumBaseTest.currentClass + "_parameters.txt\" >NUL" + "& del /s /q \"" + root
						+ "\\test-output\\\"" };

		try {
			ProcessBuilder pb = new ProcessBuilder(cmd);
			pb.redirectErrorStream(true);
			Process p = pb.start();

			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (line == null) {
					break;
				}
			}
		} catch (IOException e) {
			Assert.fail("An error occured while trying to delete old test data");
		}

		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
	}

	/**
	 * @return returns two dimensional array of {@link CucumberFeatureWrapper}
	 *         objects.
	 */
	@DataProvider
	public Object[][] features() {
		return testNGCucumberRunner.provideFeatures();
	}

	// Initializes the Selenium driver
	@BeforeMethod
	@Parameters({ "browserDriver", "user", "password" })
	public void start(Method method, @Optional("Library//webdrivers//chromedriver.exe") String browserDriver,
			@Optional String user, @Optional String password) throws Exception {

		if (executionStage.equals("Local")) {
			browser = ExcelDataConfig.getParameterValue(sheetPath, "EnvironmentDetails", "Browser");
			if (browser.equalsIgnoreCase("chrome")) {
				browser = defaultBrowser;
			} else {
				Log.printInfo("Path not configured for the browser : " + browser);
			}
		} else if (executionStage.equals("External")) {
			browser = System.getProperty("browser");
			if (browser.equalsIgnoreCase("chrome")) {
			} else {
				Log.printInfo("Path not configured for the browser : " + browser);
			}
		}

		SeleniumBaseTest.currentTest = method.getName();
		SeleniumBaseTest.currentBrowser = browser;
		SeleniumBaseTest.currentUser = user;
		SeleniumBaseTest.currentPassword = password;
		SeleniumBaseTest.browserDriver = browserDriver;
		Log.clearWorkflowSteps();
	}

	// Closes the Selenium driver and prints test information to the reporter.
	@AfterMethod
	public void end(ITestResult result, ITestContext testName) throws IOException {
		Driver.close();
	}

	@AfterTest
	public static void endParentTest() {
		tests.add(SeleniumBaseTest.currentClass);
		testNGCucumberRunner.finish();
		Driver.close();
	}

	@AfterSuite
	public static void closeExtentReport() throws IOException {
		for (String s : Reporter.getOutput()) {
			SeleniumBaseTest.extent.setTestRunnerOutput(s);
		}
		SeleniumBaseTest.extent.close();
	}

}