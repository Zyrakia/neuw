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
	public ProjectBuildException(String message) {
		super(message);
	}

}
