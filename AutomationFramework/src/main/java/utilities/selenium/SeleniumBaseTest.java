package utilities.selenium;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import utilities.exceptions.EnvironmentNotSetException;

public class SeleniumBaseTest {
	
		public static ExtentReports extent;
		public static ExtentTest parent;
		public static ExtentTest child;
		public static String environment;
		public static String currentBrowser;
		public static String currentUser;
		public static String currentPassword;
		public static String currentClass;
		public static String suiteName;
		public static String url ;
	    public static String browserDriver;
		public static String currentTest;
		public static String featureName;
		public static String scenarioName;
		public static String scenarioStatus;
		
		/**
		 * Get the environment value
		 */
		public String getEnvironment() {
			if (environment == null || environment.equals("")) {
				Log.printFatal("Environment not set. Test will exit now");
				throw new EnvironmentNotSetException();
			}
			return environment;
		}

		/**
		 * Set the environment value
		 */
		public  void setEnvironment(String environment) {
			this.environment = environment;
			Log.printInfo("Environment set to " + environment);
		}

	
}
