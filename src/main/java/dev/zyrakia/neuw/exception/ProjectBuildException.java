package dev.zyrakia.neuw.exception;

/**
 * This exception is thrown when an error occurs during project building.
 */
public class ProjectBuildException extends Exception {

	/**
	 * Creates the new exception with the given message.
	 *
	 * @param message the message to explain the exception
	 */
	private ProjectBuildException(String message) {
		super(message);
	}

	/**
	 * Wraps a given exception, prefixing it to indicate that the error occurred
	 * during the building of a project.
	 * 
	 * @param e the exception to wrap into the build exception`
	 * @return the new project build exception
	 */
	public static ProjectBuildException wrap(Exception e) {
		return new ProjectBuildException("An error occurred during project building:\n" + e.getMessage());
	}

}
