package dev.zyrakia.neuw.variable.type;

import dev.zyrakia.neuw.exception.VariableFormatException;

/**
 * Represents a variable type of {@link Object}. Parsing with this type returns
 * the input string.
 */
public class AnyVariableType extends VariableType<Object> {

    @Override
    public Object parse(String value) throws VariableFormatException {
        return String.valueOf(value);
    }

    @Override
    public Object cast(Object value) throws ClassCastException {
        return Object.class.cast(value);
    }

}
