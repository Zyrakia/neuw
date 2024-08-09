package dev.zyrakia.neuw.variable.type;

import dev.zyrakia.neuw.exception.VariableFormatException;

/**
 * Represents a variable type of {@link Integer}.
 */
public class IntVariableType extends VariableType<Integer> {

    public Integer parse(String value) throws VariableFormatException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw VariableFormatException.forExpectedType(value, Integer.class);
        }
    }

    @Override
    public Integer cast(Object value) throws ClassCastException {
        return Integer.class.cast(value);
    }

}
