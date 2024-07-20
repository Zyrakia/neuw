package dev.zyrakia.neuw.exception;

/**
 * This exception is thrown when a required variable was evaluated, but no value
 * has been set.
 */
public class UnsetRequiredVariableException extends Exception {

    /**
     * Creates a new exception for the given variable.
     * 
     * @param variableName the name of the required variable
     */
    public UnsetRequiredVariableException(String message) {
        super(message);
    }

    /**
     * Creates a new exception declaring the variable of the given name was required
     * but did not have a valid value.
     * 
     * @param name the name of the variable
     * @return the exception
     */
    public static UnsetRequiredVariableException ofName(String name) {
        return new UnsetRequiredVariableException(
                "The given variable \"" + name + "\" is required, but had no valid value set.");
    }

}
