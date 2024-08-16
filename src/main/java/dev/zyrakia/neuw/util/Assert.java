package dev.zyrakia.neuw.util;

/**
 * Utility class to assert certain conditions with an error message.
 */
public class Assert {

    /**
     * Asserts that the given object is not null, otherwise it will throw with
     * the given message.
     * 
     * @param value the value to assert
     * @param message the message if the assertion fails
     */
    public static void nonNull(Object value, String message) {
        if (value == null) throw new AssertionError(message);
    }

}
