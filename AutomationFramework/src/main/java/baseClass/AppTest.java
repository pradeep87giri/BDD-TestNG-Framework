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
import org.testng.annotations.BeforeTest;
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
 * SeleniumBaseTest must be extended by all classes in the "tests" package. It
 * provides a universal way of starting and ending test procedures. When any
 * test class is executed, EventsManagerTest will first kick off an
 * initialization method which will start the relevant browser driver, pull in
 * run time parameters, and output test information to the console and/or log.
 * When all methods tagged with "@Test" have finished executing in the test
 * classD, EventsManagerTest will run a cleanup method which closes the browser
 * driver and output test information to the console and/or log.
 */
public class AppTest extends SeleniumBaseTest implements IRetryAnalyzer {
	private int retryCount = 0;
	public int flag = 0;
	protected static int maxRetryCount = 1;
	private static String defaultBrowser = "chrome";
	public static String browser;
	private static String defaultUserAnalyst = "";
	//private static String defaultPasswordAnalyst = "";
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
	public static HashMap<String,String> data = new HashMap<String,String>();
	
	//Hashmap for selection as per role
	@Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "features")
	public void feature(CucumberFeatureWrapper cucumberFeature) {
		testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
	}

	/**
	 * Initializing Environment variables
	 * 
	 * @throws Exception
	 */
	public static void initializeEnvironment() throws Exception {
		if (executionStage.equals("Local")) {
			environment = ExcelDataConfig.getParameterValue(sheetPath, "EnvironmentDetails", "environment");
		} else if (executionStage.equals("External")) {
			environment = System.getProperty("environment");
		}

	}

	/**
	 * @return The current @Test method
	 */
	public static String getCurrentTest() {
		return SeleniumBaseTest.currentTest;

	}

	/**
	 * @return The current browser of the driver
	 */
	public static String getCurrentBrowser() {
		return SeleniumBaseTest.currentBrowser;
	}

	/**
	 * @return The current test user
	 */
	public static String getCurrentUser() {
		return SeleniumBaseTest.currentUser;
	}

	/**
	 * @return The current password for the test user
	 */
	public static String getCurrentPassword() {
		return SeleniumBaseTest.currentPassword;
	}

	/**
	 * If a test fails, this method will automatically run it again up to a
	 * specified number of times. Failed tests will be considered "Skipped" in the
	 * report if the second attempt passes.
	 */
	public boolean retry(ITestResult result) {
		if (retryCount < maxRetryCount) {
			Log.printTestRestart(result);
			retryCount++;

			return true;
		}

		return false;
	}

	/**
	 * Begins ExtentReports and creates the parentTest for the report.
	 * <p>
	 * The "@AfterSuite" method will be run before any test method belonging to the
	 * classes inside the "test" tag in the xml are run.
	 */
	@BeforeTest
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
		//SeleniumBaseTest.parent = ExtentManager.getReporter().startTest(testName.getName());
	}

	/**
	 * Set Log4j setting to get rid of warning messages that show in the console.
	 * <p>
	 * @throws Throwable 
	 * 
	 * @BeforeSuite The annotated method will be run before all tests in this suite
	 *              have run.
	 */
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
				// System.out.println(line);
			}
		} catch (IOException e) {
			Assert.fail("An error occured while trying to delete old test data");
		}

		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
		//ParameterTable.initializeDependent();
		//SeleniumBaseTest.child = ExtentTestManager.startTest(SeleniumBaseTest.currentClass);
		//initializeEnvironment();
		//setEnvironment(environment);
		//ParameterTable.readEnvironmentParameters(getEnvironment());

	}

	/**
	 * @return returns two dimensional array of {@link CucumberFeatureWrapper}
	 *         objects.
	 */
	@DataProvider
	public Object[][] features() {
		return testNGCucumberRunner.provideFeatures();
	}

	/**
	 * Initializes the Selenium driver, retrieves all run time parameters from the
	 * TestNG XML (or reverts to default settings if they cannot be found). Logs
	 * into the system under test and prints test information to the reporter.
	 * <p>
	 * The "@BeforeMethod" tag informs TestNG that this method should be run before
	 * all methods tagged with "@Test"
	 * 
	 * @param method
	 *            A String of the test method being executed
	 * @param browser
	 *            A String of the browser environment to be tested
	 * @param user
	 *            A String of the name of the user logging into the system under
	 *            test
	 * @param password
	 *            A String of the password of the user logging into the system under
	 *            test
	 * @throws Exception
	 */
	@BeforeMethod
	@Parameters({"browserDriver", "user", "password" })
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
		/*if (browser.equals("${test_browser}"))
			browser = defaultBrowser;

		else if (method.getName().equals("someMethodName") && user.equals("${test_user_analyst}")
				&& password.equals("${test_password_analyst}")) {
			user = defaultUserAnalyst;
			password = defaultPasswordAnalyst;
		} else {
			System.out.println("Inside");
			user = System.getProperty("alUsername");
			password = System.getProperty("alPassword");

		}*/
		// ********************************************************************************************************************************

		SeleniumBaseTest.currentTest = method.getName();
		SeleniumBaseTest.currentBrowser = browser;
		SeleniumBaseTest.currentUser = user;
		SeleniumBaseTest.currentPassword = password;
		SeleniumBaseTest.browserDriver = browserDriver;
		
		//SeleniumBaseTest.url = ParameterTable.get("appURL");

		Log.clearWorkflowSteps();
		

		//Driver.initialize(browser, browserDriver);
		//Driver.Instance.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		// Driver.Instance.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		//Driver.Instance.manage().window().maximize();
		//LoginPage.goTo();
		/*
		 * if(executionStage.equals("External")){ LoginPage.loginAuto(user,password); }
		 */
		//Driver.waitForAjaxToComplete();

	}
	

	/**
	 * Closes the Selenium driver and prints test information to the reporter.
	 * <p>
	 * The "@AfterMethod" tag informs TestNG that this method should be run after
	 * each of methods tagged with "@Test" have completed.
	 * 
	 * @throws IOException
	 */
	@AfterMethod
	public void end(ITestResult result, ITestContext testName) throws IOException {
		Driver.close();

	}

	/**
	 * Ends the parentTest for ExtentReports and prints out the log to the report.
	 * <p>
	 * The "@AfterTest" method will be run after any test method belonging to the
	 * classes inside the "test" tag in the xml are run.
	 * 
	 */
	@AfterTest
	public static void endParentTest() {
		tests.add(SeleniumBaseTest.currentClass);
		/*SeleniumBaseTest.extent.endTest(SeleniumBaseTest.parent);
		SeleniumBaseTest.extent.flush();*/
		testNGCucumberRunner.finish();
		Driver.close();
	}

	/**
	 * Ends ExtentReports and prints out the full log to the report.
	 * <p>
	 * The "@AfterSuite" tag informs TestNG that this method should be run after the
	 * xml suite has ended.
	 * 
	 * @throws IOException
	 */
	@AfterSuite
	public static void closeExtentReport() throws IOException {

		for (String s : Reporter.getOutput()) {
			SeleniumBaseTest.extent.setTestRunnerOutput(s);
		}

	}

	

}