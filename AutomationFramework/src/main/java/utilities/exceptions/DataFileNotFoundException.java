package utilities.exceptions;


public class DataFileNotFoundException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	public DataFileNotFoundException(String dataFileLocation) {
		super(
				"DataFile required for the test is not found the given location:\n"
						+ dataFileLocation
						+ "\nPlease check the location. Test will exit");
	}
}
