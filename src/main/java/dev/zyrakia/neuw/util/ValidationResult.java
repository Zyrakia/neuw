package dev.zyrakia.neuw.util;

import dev.zyrakia.neuw.exception.ValidationException;

/**
 * Encapsulate the result of a validation in any context.
 * 
 * @param isValid whether the validation was successful
 * @param message the message detailing the validation success or failure
 */
public record ValidationResult(boolean isValid, String message) {

    public void assertIsValid() throws ValidationException {
        if (this.isValid)
            return;

        throw new ValidationException(this.message);
    }

    /**
     * Creates a new successful validation result, with the given message.
     * 
     * @param message the concise message detailing the success
     * @return the created validation result
     */
    public static ValidationResult success(String message) {
        return new ValidationResult(true, message);
    }

    /**
     * Creates a new successful validation result.
     * 
     * @return the created validation result
     */
    public static ValidationResult success() {
        return ValidationResult.success("");
    }

    /**
     * Creates a new successful validation result, with the given message.
     * 
     * @param message the concise message detailing the validation
     * @return the created validation result
     */
    public static ValidationResult ok(String message) {
        return ValidationResult.success(message);
    }

    /**
     * Creates a new successful validation result.
     * 
     * @return the created validation result
     */
    public static ValidationResult ok() {
        return ValidationResult.success();
    }

    /**
     * Creates a new failed validation result.
     * 
     * @param message the concise message detailing the failure
     * @return the created validation result
     */
    public static ValidationResult fail(String message) {
        return new ValidationResult(false, message);
    }

}
