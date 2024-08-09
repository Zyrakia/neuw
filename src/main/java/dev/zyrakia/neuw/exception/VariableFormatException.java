package dev.zyrakia.neuw.exception;

/**
 * Thrown to indicate that a variable value could not be parsed out of a given
 * piece of input.
 */
public class VariableFormatException extends IllegalArgumentException {

    /**
     * Creates a new exception with the given message.
     * 
     * @param message the message
     */
    public VariableFormatException(String message) {
        super(message);
    }

    /**
     * Returns a new exception that describes the parsing error between the
     * given input and the given expected type.
     * 
     * @param input the input that was not parsed
     * @param expectedType the expected type that could not be parsed out of the
     * input
     * @return the created exception
     */
    public static VariableFormatException forExpectedType(String input,
            Class<?> expectedType) {
        return new VariableFormatException("The given input \"" + input
                + "\" could not be parsed into the type \""
                + expectedType.getSimpleName() + "\".");
    }

}
