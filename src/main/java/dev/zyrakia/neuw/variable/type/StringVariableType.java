package dev.zyrakia.neuw.variable.type;

import dev.zyrakia.neuw.exception.VariableFormatException;

/**
 * Represents a variable type of {@link String}.
 */
public class StringVariableType extends VariableType<String> {

    @Override
    public String parse(String value) throws VariableFormatException {
        return String.valueOf(value);
    }

    @Override
    public String cast(Object value) throws ClassCastException {
        return String.class.cast(value);
    }

}
