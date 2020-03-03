
package utilities.parameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.relevantcodes.extentreports.LogStatus;

import baseClass.AppTest;
import utilities.exceptions.DataFileNotFoundException;
import utilities.exceptions.EnvironmentParametersNotFoundException;
import utilities.selenium.ExcelDataConfig;
import utilities.selenium.Log;
import utilities.selenium.SeleniumBaseTest;

/**
 * This class creates a hash table that can be used throughout the entire test.
 * The value can be saved as a String to a parameter and pass to other Steps.
 */
public class ParameterTable extends AppTest {
	private static Hashtable<String, String> StringTable;
	private static Hashtable<String, List<String>> StringListTable;
	private static String root = System.getProperty("user.dir") + "\\src\\test\\resources\\ParameterFiles\\";
	public static String URL;

	/**
	 * Creates the hash table to be used with the test. Also creates the necessary
	 * file to store all of the parameters and values throughout the test. This can
	 * be used for debugging purposes as well.
	 */
	public static void initialize() {
		StringTable = new Hashtable<String, String>();
		StringListTable = new Hashtable<String, List<String>>();

		try {
			FileReader parameterReader = new FileReader(root + "parameters.txt");
			BufferedReader bufferedParameterReader = new BufferedReader(parameterReader);
			String line, parameter, value;
			List<String> values = new ArrayList<String>();
			boolean found = false;

			while ((line = bufferedParameterReader.readLine()) != null) {
				if (line.equals("*") || found) {
					found = true;
					if (line.equals("*")) {
						// do nothing - skip to next line
					} else {
						Pattern p = Pattern.compile("\\<(.*?)\\>");
						Matcher m = p.matcher(line);

						if (m.find()) {
							parameter = m.group(1);
							while (m.find()) {
								values.add(m.group(1));
							}

							StringListTable.put(parameter, new ArrayList<String>(values));
							values.clear();
						}
					}
				}

				else {
					Pattern p = Pattern.compile("\\<(.*?)\\>");
					Matcher m = p.matcher(line);

					while (m.find()) {
						parameter = m.group(1);
						if (m.find()) { // Check that Matcher finds a group.
							value = m.group(1);
							StringTable.put(parameter, value);
						}

					}

				}

				bufferedParameterReader.close();
				parameterReader.close();

			}
		}

		catch (IOException e) {
			Log.printAction("'parameters.txt' does not exist, creating a new one");
		}
	}

	public static void initializeDependent() {
		StringTable = new Hashtable<String, String>();
		StringListTable = new Hashtable<String, List<String>>();

		try {
			FileReader parameterReader = new FileReader(root + SeleniumBaseTest.currentClass + "_parameters.txt");
			BufferedReader bufferedParameterReader = new BufferedReader(parameterReader);
			String line, parameter, value;
			List<String> values = new ArrayList<String>();
			boolean found = false;

			while ((line = bufferedParameterReader.readLine()) != null) {
				if (line.equals("*") || found) {
					found = true;
					if (line.equals("*")) {
						// do nothing - skip to next line
					} else {
						Pattern p = Pattern.compile("\\<(.*?)\\>");
						Matcher m = p.matcher(line);

						if (m.find()) {
							parameter = m.group(1);
							while (m.find()) {
								values.add(m.group(1));
							}

							StringListTable.put(parameter, new ArrayList<String>(values));
							values.clear();
						}
					}
				}

				else {
					Pattern p = Pattern.compile("\\<(.*?)\\>");
					Matcher m = p.matcher(line);

					while (m.find()) {
						parameter = m.group(1);
						m.find();
						value = m.group(1);
						StringTable.put(parameter, value);
					}

				}

			}

			bufferedParameterReader.close();
			parameterReader.close();

		}

		catch (IOException e) {
			Log.printAction("'parameters.txt' does not exist, creating a new one");
		}
	}

	/**
	 * This method reads the credentials XML file for a particular environment and
	 * adds it to the dataTable.
	 */
	public static void readEnvironmentParameters(String environment) throws Exception {
		if (environment.equalsIgnoreCase("UAT")) {
			URL = ExcelDataConfig.getParameterValue(sheetPath, "EnvironmentDetails", "URL-QA");

		} else if (environment.equalsIgnoreCase("DV")) {
			URL = ExcelDataConfig.getParameterValue(sheetPath, "EnvironmentDetails", "URL-DV");
		} else {
			System.out.println("Specify Environment Correctly : PV/DV");
		}
		ParameterTable.add("environment", environment);
		ParameterTable.add("appURL", URL);
	}

	/**
	 * This method reads the credentials XML file for a particular environment and
	 * adds it to the dataTable.
	 */
	public static void readEnvironmentParameters(String environment, String location) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document document = null;

			try {
				document = dBuilder.parse(new File(location));

				Log.printInfo("Environment Parameter XML read at " + location);
			} catch (IOException e) {
				location = "src/test/resources/EnvironmentParameters/envParameters.xml";
				document = dBuilder.parse(new File(location));
				Log.printInfo("Environment Parameter XML read at " + location);
			}

			document.getDocumentElement().normalize();
			NodeList environmentNodeList = document.getElementsByTagName("environment");

			boolean isFound = false;
			for (int count = 0; count < environmentNodeList.getLength(); count++) {
				Node environmentNode = environmentNodeList.item(count);

				if (environmentNode.getNodeType() == Node.ELEMENT_NODE) {
					Element environmentElement = (Element) environmentNode;
					if (environmentElement.getAttribute("id").equalsIgnoreCase(environment)) {
						ParameterTable.add("environment", environment);
						NodeList parameterNodeList = environmentElement.getChildNodes();
						for (int parameterCount = 0; parameterCount < parameterNodeList.getLength(); parameterCount++) {
							Node parameterNode = parameterNodeList.item(parameterCount);
							if (parameterNode.getNodeType() == Node.ELEMENT_NODE) {
								ParameterTable.add(parameterNode.getNodeName(), parameterNode.getTextContent());

							}
						}
						isFound = true;
						break;
					}
				}
			}

			if (!isFound) {
				Log.printInfo("Environment parameters not found for Environment, " + environment);
				throw new EnvironmentParametersNotFoundException(environment);
			}
		} catch (ParserConfigurationException e) {
			Log.printInfo("Error Reading Environment Parameter Sheet at " + location);
			throw new EnvironmentParametersNotFoundException(environment);
		} catch (SAXException e) {
			Log.printInfo("Error Reading Environment Parameter Sheet at " + location);
			throw new EnvironmentParametersNotFoundException(environment);
		} catch (IOException e) {
			Log.printInfo("Error Reading Environment Parameter Sheet at " + location);
			throw new DataFileNotFoundException(location);
		}
	}

	/**
	 * Prints the hash table to a text file with all of the parameters and values.
	 */
	public static void printToFile() {
		PrintWriter pw;

		try {
			pw = new PrintWriter(root + SeleniumBaseTest.currentClass + "_parameters.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}

		for (Map.Entry<String, String> entry : StringTable.entrySet()) {
			String parameter = entry.getKey();
			String value = entry.getValue();
			String pair = "<" + parameter + "> <" + value + ">";

			if (!parameter.equals(null))
				pw.println(pair);
		}

		pw.println("*");

		for (Map.Entry<String, List<String>> entry : StringListTable.entrySet()) {
			String parameter = entry.getKey();
			List<String> values = entry.getValue();
			String value = "";
			for (String eachValue : values)
				value = value + " <" + eachValue + ">";
			String pair = "<" + parameter + ">" + value;

			if (!parameter.equals(null))
				pw.println(pair);
		}

		pw.close();
	}

	/**
	 * For debugging purposes. Prints out the hash table in the console.
	 */
	public static void printToConsole() {
		for (Map.Entry<String, String> entry : StringTable.entrySet()) {
			String parameter = entry.getKey();
			String value = entry.getValue();
			String pair = "<" + parameter + "> <" + value + ">";

			if (!parameter.equals(null))
				System.out.println(pair);
		}

		System.out.println("*");

		for (Map.Entry<String, List<String>> entry : StringListTable.entrySet()) {
			String parameter = entry.getKey();
			List<String> values = entry.getValue();
			String value = "";
			for (String eachValue : values)
				value = value + " <" + eachValue + ">";
			String pair = "<" + parameter + ">" + value;

			if (!parameter.equals(null))
				System.out.println(pair);
		}
	}

	/**
	 * Prints the hash table to ExtentReports with all of the parameters and values.
	 */
	public static void printToReport() {
		StringBuilder logMessage = new StringBuilder("");

		logMessage.append("## Printing Test Data ##<br><br>");

		for (Map.Entry<String, String> entry : StringTable.entrySet()) {
			String parameter = entry.getKey();
			String value = entry.getValue();
			String pair = parameter + " = " + value;

			if (!parameter.equals(null))
				logMessage.append(pair + "<br>");
		}

		logMessage.append("*<br>");

		for (Map.Entry<String, List<String>> entry : StringListTable.entrySet()) {
			String parameter = entry.getKey();
			List<String> values = entry.getValue();
			String value = "";
			for (String eachValue : values)
				value = value + "[" + eachValue + "] ";
			String pair = parameter + " = " + value;

			if (!parameter.equals(null))
				logMessage.append(pair + "<br>");
		}

		SeleniumBaseTest.child.log(LogStatus.INFO, "");
		SeleniumBaseTest.child.log(LogStatus.INFO, logMessage.toString());
	}

	public static String printToLog() {
		Log.printTestData();

		StringBuilder logMessage = new StringBuilder("");

		for (Map.Entry<String, String> entry : StringTable.entrySet()) {
			String parameter = entry.getKey();
			String value = entry.getValue();
			String pair = "<" + parameter + "> <" + value + ">";

			if (!parameter.equals(null))
				logMessage.append(pair + "\n");
		}

		logMessage.append("\n" + "*");

		for (Map.Entry<String, List<String>> entry : StringListTable.entrySet()) {
			String parameter = entry.getKey();
			List<String> values = entry.getValue();
			String value = "";
			for (String eachValue : values)
				value = value + " <" + eachValue + ">";
			String pair = "<" + parameter + ">" + value;

			if (!parameter.equals(null))
				logMessage.append(pair + "\n");
		}

		return logMessage.toString();
	}

	/**
	 * Adds a new parameter with a value. It can also overwrite a parameter with a
	 * previous value already in storage.
	 */
	public static void add(String parameterName, String value) {
		Log.printAction("Parameter added: '" + parameterName + "' with value: '" + value + "'");
		StringTable.put(parameterName, value);
	}

	/**
	 * Gets the value of a parameter.
	 */
	public static String get(String parameterName) {
		Log.printAction("Retrieving parameter: '" + parameterName + "' = " + StringTable.get(parameterName));
		return StringTable.get(parameterName);
	}

	/**
	 * Removes the specified parameter from the parameter table.
	 */
	public static String remove(String parameterName) {
		Log.printAction("Parameter removed: '" + parameterName);
		return StringTable.remove(parameterName);
	}

	public static boolean containsParameter(String parameterName) {
		return StringTable.containsKey(parameterName);
	}

	public static boolean containsValue(String value) {
		return StringTable.containsValue(value);
	}

	public static void addList(String parameterName, List<String> values) {
		Log.printAction("Parameter added: '" + parameterName + "' with values: '" + values + "'");
		StringListTable.put(parameterName, values);
	}

	public static List<String> getList(String parameterName) {
		Log.printAction("Retrieving parameter: '" + parameterName + "'");
		return StringListTable.get(parameterName);
	}

	public static List<String> removeList(String parameterName) {
		Log.printAction("Parameter removed: '" + parameterName);
		return StringListTable.remove(parameterName);
	}

	public static boolean containsListParameter(String parameterName) {
		return StringListTable.containsKey(parameterName);
	}

	public static boolean containsListValue(String value) {
		return StringListTable.containsValue(value);
	}
}
