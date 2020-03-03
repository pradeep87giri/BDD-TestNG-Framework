package utilities.exceptions;


public class EnvironmentParametersNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EnvironmentParametersNotFoundException(String environment) {
		super("Environment parameters not found for environment, "
				+ environment + ". Test will exit");
	}
}
