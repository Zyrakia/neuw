package dev.zyrakia.neuw.variable.type;

import dev.zyrakia.neuw.exception.VariableFormatException;

/**
 * Represents a variable type of {@link Double}.
 */
public class DoubleVariableType extends VariableType<Double> {

    @Override
    public Double parse(String value) throws VariableFormatException {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw VariableFormatException.forExpectedType(value, Double.class);
        }
    }

    @Override
    public Double cast(Object value) throws ClassCastException {
        return Double.class.cast(value);
    }

}