package dev.zyrakia.neuw.exception;

/**
 * This exception is thrown to indicate that the value of a variable did not match the type that the variable holds,
 * either the base type or one of the rules.
 */
public class VariableTypeMismatchException extends IllegalArgumentException {

	/**
	 * Creates the new exception with the given message.
	 *
	 * @param message the message to explain the exception
	 */
	public VariableTypeMismatchException(String message) {
		super(message);
	}

}
