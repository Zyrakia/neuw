package dev.zyrakia.neuw.variable;

import dev.zyrakia.neuw.exception.UnsetRequiredVariableException;
import dev.zyrakia.neuw.exception.ValidationException;
import dev.zyrakia.neuw.util.ValidationResult;
import dev.zyrakia.neuw.variable.type.VariableType;

/**
 * Represents a variable that can be used to hold a typed piece of data.
 * 
 * @param identifier   the identifier of the variable, which is what it will be
 *                     referenced with
 * @param name         the user-friendly name of the variable for display
 *                     purposes
 * @param description  a concise description of how the variable is implemented
 *                     for display purposes
 * @param required     whether the variable is required or not
 * @param type         the type of the variable
 * @param defaultValue the default value of this variable
 */
public record Variable<T>(String identifier, String name, String description, boolean required, VariableType<T> type,
        T defaultValue) {

    public Variable {
        if (defaultValue != null) {
            ValidationResult defaultValueRes = type.validate(defaultValue);
            if (!defaultValueRes.isValid())
                throw new IllegalArgumentException("The default value of the variable \"" + name
                        + "\" did not pass validation: \"" + defaultValueRes.message() + "\"");
        }
    }

    /**
     * An instance of this variable which can hold a value.
     * 
     * Instances can hold any type of value and if the value
     * is not valid, it will simply evaluate to {@code null}.
     */
    public class Instance {

        /**
         * The currently set value.
         */
        private T value;

        /**
         * Creates a new variable without an initial value.
         */
        public Instance() {
        }

        /**
         * Creates a new variable with the given initial value.
         * 
         * @throws ValidationException if the given initial value does not match the
         *                             type of this variable
         */
        public Instance(Object initialValue) throws ValidationException {
            this.setValue(initialValue);
        }

        /**
         * Sets the current value of this variable by first casting it into the required
         * type. If the given value is null, it will set the current value to null,
         * avoiding validation.
         * 
         * @param value the new value of this variable
         * @throws ValidationException if the given value does not match the type of
         *                             this variable
         */
        public void setValue(Object value) throws ValidationException {
            if (value == null)
                this.value = null;
            else {
                type.validate(value).assertIsValid();
                this.value = type.cast(value);
            }
        }

        /**
         * Evaluates the current value of this variable, if there is no valid value or
         * default value, null will be returned. If the variable is required however; an
         * error will be thrown.
         * 
         * @return the evaluated value, or null if no valid value was set
         * @throws UnsetRequiredVariableException if the variable had no valid value
         *                                        set, but is marked as required
         */
        public T evaluate() throws UnsetRequiredVariableException {
            T value = this.value == null ? defaultValue : this.value;
            if (value == null || !type.validate(value).isValid()) {
                if (required)
                    throw UnsetRequiredVariableException.ofName(name);
                else
                    return null;
            }

            return value;
        }

    }

    /**
     * Creates a new instance of this variable.
     * 
     * @return the created instance
     */
    public Variable<T>.Instance instantiate() {
        return new Instance();
    }

}
