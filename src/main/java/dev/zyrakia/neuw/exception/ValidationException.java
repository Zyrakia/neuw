package dev.zyrakia.neuw.exception;

/**
 * Thrown to indicate that a validation did not reach the desired result.
 */
public class ValidationException extends Exception {

    /**
     * Creates the new exception with the given message.
     * 
     * @param message the message describing why the validation did not reach the
     *                desired result
     */
    public ValidationException(String message) {
        super(message);
    }

}
