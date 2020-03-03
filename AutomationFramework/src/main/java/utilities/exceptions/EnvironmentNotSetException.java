package utilities.exceptions;


public class EnvironmentNotSetException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EnvironmentNotSetException() {
		super(
				"Environment name is not set in the test. Please set this for test to continue.");
	}
}
