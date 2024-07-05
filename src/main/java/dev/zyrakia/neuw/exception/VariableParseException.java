package dev.zyrakia.neuw.exception;

/**
 * This exception is thrown to indicate that a value was not able to be parsed into a given type.
 */
public class VariableParseException extends IllegalArgumentException {

	/**
	 * Creates the new exception with the given message.
	 *
	 * @param message the message to explain the exception
	 */
	public VariableParseException(String message) {
		super(message);
	}

}
