package stepDefinitions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

import baseClass.AppTest;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import utilities.extentreport.ExtentManager;
import utilities.extentreport.ExtentTestManager;
import utilities.parameters.ParameterTable;
import utilities.selenium.Driver;
import utilities.selenium.ExcelDataConfig;
import utilities.selenium.Log;
import utilities.selenium.SeleniumBaseTest;

public class Hooks extends AppTest{
	
	static String endTime;
	static String startTime;
	static int count = 0;
	@Before

	public void start(Scenario scenario) throws Exception {
		Log.clearWorkflowSteps();
		Calendar cal = Calendar.getInstance();
	    Date date=cal.getTime();
	    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	    startTime=dateFormat.format(date);
	    System.out.println("Start time is : " + startTime);
		SeleniumBaseTest.featureName = scenario.getId().split(";")[0];

		SeleniumBaseTest.featureName = (SeleniumBaseTest.featureName.substring(0, 1).toUpperCase()
				+ SeleniumBaseTest.featureName.substring(1).toLowerCase()).replace("-", "_");

		System.out.println("Feature Name : " + SeleniumBaseTest.featureName);
		System.out.println("Sc Name : " + scenario.getName());

		SeleniumBaseTest.scenarioName = scenario.getName();

		SeleniumBaseTest.parent = ExtentManager.getReporter().startTest(SeleniumBaseTest.scenarioName);

		String root = System.getProperty("user.dir");

		String[] cmd = { "cmd.exe", "/c",
				"del \"" + root + "\\src\\test\\resources\\ParameterFiles\\" + SeleniumBaseTest.scenarioName
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

		ParameterTable.initializeDependent();
		SeleniumBaseTest.child = ExtentTestManager.startTest(SeleniumBaseTest.scenarioName);

		AppTest.initializeEnvironment();

		AppTest app = new AppTest();
		app.setEnvironment(app.environment);
		ParameterTable.readEnvironmentParameters(app.getEnvironment());
		SeleniumBaseTest.url = ParameterTable.get("appURL");

		Log.printScenarioStart();
		Driver.initialize(SeleniumBaseTest.currentBrowser, SeleniumBaseTest.browserDriver);
		Driver.Instance.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Driver.Instance.get(SeleniumBaseTest.url);
	}

	@After
	public static void end(Scenario scenario) throws Exception {

		ParameterTable.printToFile();
		Log.printScenarioEnd();
		SeleniumBaseTest.scenarioStatus = scenario.getStatus();

		if (SeleniumBaseTest.scenarioStatus.equals("skipped")) {
			AppTest.skippedtests.add(SeleniumBaseTest.scenarioName);
			Log.takeScreenshot();
			SeleniumBaseTest.child.log(LogStatus.SKIP, "Error found. Retrying the test...<br><br>");
		} else if (SeleniumBaseTest.scenarioStatus.equals("failed")) {
			Log.takeScreenshotOnFailure();
			Driver.close();
			AppTest.failedtests.add(SeleniumBaseTest.scenarioName);
			SeleniumBaseTest.child.log(LogStatus.FAIL, "Test Failed");
		} else if (SeleniumBaseTest.scenarioStatus.equals("passed")) {
			AppTest.passedtests.add(SeleniumBaseTest.scenarioName);
			Driver.close();
			SeleniumBaseTest.child.log(LogStatus.PASS, "Test completed successfully");
		}
		Calendar cal = Calendar.getInstance();
	    Date date=cal.getTime();
	    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	    endTime=dateFormat.format(date);
	    System.out.println("End time is : " + endTime);
	    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	    Date date1 = format.parse(startTime);
	    Date date2 = format.parse(endTime);
	    long difference = (date2.getTime() - date1.getTime())/1000; 
	    int p1 = (int) (difference % 60);
	    int p2 = (int) (difference / 60);
		int p3 = p2 % 60;
		String timeTaken = p3 + "m" + p1 + "s";
		System.out.print("MM:SS - " + p3 + "m" + p1 + "s");

		child.setDescription(Log.getWorkflowSteps());
		SeleniumBaseTest.parent.appendChild(SeleniumBaseTest.child);
		ParameterTable.printToFile();

		SeleniumBaseTest.extent.endTest(SeleniumBaseTest.parent);
		SeleniumBaseTest.extent.flush();

	}


}
