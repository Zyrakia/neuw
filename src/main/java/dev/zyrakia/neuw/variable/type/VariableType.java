package dev.zyrakia.neuw.variable.type;

import java.util.ArrayList;
import java.util.List;

import dev.zyrakia.neuw.exception.VariableFormatException;
import dev.zyrakia.neuw.util.ValidationResult;
import dev.zyrakia.neuw.variable.type.rule.VariableTypeRule;

/**
 * Represents the type of a variable, the initial validation is done with a
 * cast, but validation can be more detailed via added {@link VariableTypeRule}
 * instances.
 */
public abstract class VariableType<T> {

    /**
     * The rules that further narrow down this variable type.
     */
    protected List<VariableTypeRule<T>> rules = new ArrayList<>();

    /**
     * Parses the base type {@link T} out of the given string.
     * 
     * @param value the string value to parse
     * @return the parsed value
     * @throws VariableFormatException if the given value cannot be parsed into
     * {@link T}
     */
    public abstract T parse(String value) throws VariableFormatException;

    /**
     * Attempts to cast the given value into the base type {@link T}.
     * 
     * @param value the value to cast
     * @return the casted value
     * @throws ClassCastException if the given value cannot be cast
     */
    public abstract T cast(Object value) throws ClassCastException;

    /**
     * Validates the given value against this type, including all of the rules.
     * 
     * @param value the value to validate
     * @return the result of the validation
     */
    public ValidationResult validate(Object value) {
        try {
            T castedValue = this.cast(value);
            return this.validateRules(castedValue);
        } catch (ClassCastException e) {
            return ValidationResult.fail("The given value \"" + value
                    + "\" could not be cast into the variable type \""
                    + this.getClass().getName() + ".\"");
        }
    }

    /**
     * Validates the given value against the rules of this type.
     * 
     * @param value the value to validate
     * @return the result of the validation
     */
    private ValidationResult validateRules(T value) {
        for (VariableTypeRule<T> rule : this.rules) {
            if (rule.validate(value)) continue;

            return ValidationResult.fail("The value \"" + value
                    + "\" did not follow the rule \"" + rule + ".\"");
        }

        return ValidationResult.ok();
    }

    /**
     * Adds the given rule to the list of rules of this type.
     * 
     * @param rule the rule that will add to the validation of this type
     * @return this instance
     */
    public VariableType<T> addRule(VariableTypeRule<T> rule) {
        this.rules.add(rule);
        return this;
    }

    /**
     * Removes the guiven rule from the list of rules of this type.
     * 
     * @param rule the rule that will remove from the validation of this type
     * @return this instance
     */
    public VariableType<T> removeRule(VariableTypeRule<T> rule) {
        this.rules.remove(rule);
        return this;
    }

}
