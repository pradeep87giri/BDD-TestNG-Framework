
package utilities.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Properties;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import baseClass.AppTest;
import stepDefinitions.Hooks;

/**
 * This class reads the data contained in the worksheet of an Excel file It uses
 * POI library utilities to a handle Excel file "key" column contains the
 * attribute and "value" column contains the value of the attribute
 * 
 */

public class ExcelDataConfig {
	
	private static Properties properties = new Properties();

	public static void initialize(String filePath, String sheetName) throws Exception {
		XSSFWorkbook excelWBook = null;

		try {

			// Access the required test data sheet
			excelWBook = new XSSFWorkbook(new FileInputStream(filePath));
			XSSFSheet configWSheet = excelWBook.getSheet(sheetName);

			importPropertiesFromXLS(configWSheet);
		} finally {
			if (excelWBook != null) {
				excelWBook.close();
			}
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	private static void importPropertiesFromXLS(XSSFSheet configWSheet) {
		properties.clear();
		int rowCount = configWSheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rowCount; i++) {
			String key = getCellValue(configWSheet, i, 0);
			String value = getCellValue(configWSheet, i, 1);
			if (key != null && value != null) {
				addProperty(key, value);
			}
		}

	}

	private static String getCellValue(XSSFSheet sheet, int rownum, int cellnum) {
		String value = "";
		XSSFCell cell = sheet.getRow(rownum).getCell(cellnum);
		if (cell == null) {
			return null;
		}
		if (cell.getCellType() == CellType.NUMERIC) {
			value = String.valueOf(cell.getNumericCellValue());
		} else if (cell.getCellType() == CellType.STRING) {
			value = cell.getStringCellValue();
		} else if (cell.getCellType() == CellType.BOOLEAN) {
			value = String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == CellType.FORMULA) {
			if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
				value = String.valueOf(cell.getNumericCellValue());
			} else if (cell.getCachedFormulaResultType() == CellType.STRING) {
				value = cell.getStringCellValue();
			} else if (cell.getCachedFormulaResultType() == CellType.BOOLEAN) {
				value = String.valueOf(cell.getBooleanCellValue());
			}
		}
		return value;

	}

	private static void addProperty(String key, String value) {
		properties.put(key, value);
	}

	public static String getParameterValue(String filePath, String sheetName, String parameterName) throws Exception {
		String parameterVal = null;

		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);

		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sh = wb.getSheet(sheetName);

		int totalRows = sh.getLastRowNum();
		int totalCols = sh.getRow(0).getLastCellNum();
		// System.out.println("Total no of Rows : " + totalRows);
		// System.out.println("Total no of Columns : " + totalCols);
		for (int i = 1; i <= totalRows; i++) {
			if (parameterName.equalsIgnoreCase(sh.getRow(i).getCell(0).getStringCellValue())) {
				try {
					parameterVal = sh.getRow(i).getCell(1).getStringCellValue();
					break;
				} catch (Exception e) {
					parameterVal = String.valueOf((int) (sh.getRow(i).getCell(1).getNumericCellValue()));
					break;
				}

			}
			wb.close();
		}
		return parameterVal;
	}

	public static String getParameterValueUsingColumnName(String filePath, String sheetName, String parameterName,
			String columnName) throws Exception {
		String parameterVal = null;

		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);

		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sh = wb.getSheet(sheetName);

		int totalRows = sh.getLastRowNum();
		int totalCols = sh.getRow(0).getLastCellNum();
		// System.out.println("Total no of Rows : " + totalRows);
		// System.out.println("Total no of Columns : " + totalCols);

		for (int i = 1; i <= totalRows; i++) {
			if (parameterName.equalsIgnoreCase(sh.getRow(i).getCell(0).getStringCellValue())) {
				for (int j = 0; j <= totalCols - 1; j++) {
					if (columnName.equalsIgnoreCase(sh.getRow(0).getCell(j).getStringCellValue())) {
						try {
							parameterVal = sh.getRow(i).getCell(j).getStringCellValue();
							Log.printInfo(columnName + " value is " + parameterVal);
							break;
						} catch (Exception e) {
							parameterVal = String.valueOf((int) (sh.getRow(i).getCell(j).getNumericCellValue()));
							Log.printInfo(columnName + " value is " + parameterVal);
							break;
						}

					}
				}
				break;
			}
		}
		wb.close();
		return parameterVal;
	}

	public static String checkParameterIsPresent(String filePath, String sheetName, String parameterName)
			throws Exception {

		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);

		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sh = wb.getSheet(sheetName);

		int totalRows = sh.getLastRowNum();
		int totalCols = sh.getRow(0).getLastCellNum();
		String booleanValue = null;
		
		for (int i = 1; i < totalRows; i++) {
			if (parameterName.equalsIgnoreCase(sh.getRow(i).getCell(0).getStringCellValue())) {
				booleanValue = "true";
				break;
			} else {
				booleanValue = "false";
			}

		}
		wb.close();
		return booleanValue;
	}

	public static void writeOnExcel(String filePath, String sheetName, String parameterName, String value)
			throws Exception {
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);

		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sh = wb.getSheet(sheetName);

		int totalRows = sh.getLastRowNum();
		int totalCols = sh.getRow(0).getLastCellNum();
		// System.out.println("Total no of Rows : " + totalRows);
		// System.out.println("Total no of Columns : " + totalCols);
		String booleanValue = "false";
		for (int i = 1; i <= totalRows; i++) {
			if (parameterName.equalsIgnoreCase(sh.getRow(i).getCell(0).getStringCellValue())) {
				booleanValue = "true";
				break;
			}
		}
		if (booleanValue.equals("false")) {
			Row row = sh.createRow(++totalRows);
			int columnCount = 0;
			Cell cell = row.createCell(columnCount);
			cell.setCellValue(value);
			cell = row.createCell(++columnCount);
			cell.setCellValue("null");

			fis.close();

			FileOutputStream outputStream = new FileOutputStream(file);
			wb.write(outputStream);
			wb.close();
			outputStream.close();

		}

		wb.close();

	}

	public static void createExcelSheet(String fileName, String sheetName, String scenarioName, String orderNumber,
			String timeTaken, String status) throws Exception {

		File file = new File(fileName);

		if (file.exists()) {

			File file1 = new File("//craft//AutomationFramework//" + fileName);
			FileInputStream fis = new FileInputStream(file);

			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sh = wb.getSheet(sheetName);

			int totalRows = sh.getLastRowNum();
			int totalCols = sh.getRow(0).getLastCellNum();

			Row row = sh.createRow(++totalRows);
			int columnCount = 0;
			Cell cell = row.createCell(columnCount);
			cell.setCellValue(scenarioName);
			cell = row.createCell(++columnCount);
			cell.setCellValue(orderNumber);
			cell = row.createCell(columnCount + 1);
			cell.setCellValue(status);
			cell = row.createCell(columnCount + 2);
			cell.setCellValue(timeTaken);

			fis.close();

			FileOutputStream outputStream = new FileOutputStream(file);
			wb.write(outputStream);
			wb.close();
			outputStream.close();

		} else {

			FileOutputStream fos = new FileOutputStream(fileName);
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sh = wb.createSheet(sheetName);

			int rowCount = 0;
			Row row = sh.createRow(rowCount);
			int columnCount = 0;
			Cell cell = row.createCell(columnCount);
			cell.setCellValue(scenarioName);
			cell = row.createCell(++columnCount);
			cell.setCellValue(orderNumber);
			cell = row.createCell(columnCount + 1);
			cell.setCellValue(status);
			cell = row.createCell(columnCount + 2);
			cell.setCellValue(timeTaken);

			wb.write(fos);
			fos.flush();
			fos.close();
			wb.close();
		}
	}
	
	
}