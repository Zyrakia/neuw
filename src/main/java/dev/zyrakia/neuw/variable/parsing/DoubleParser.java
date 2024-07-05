package dev.zyrakia.neuw.variable.parsing;

import dev.zyrakia.neuw.exception.VariableParseException;

/**
 * Responsible for parsing double values for variables.
 */
public class DoubleParser implements VariableParser<Double> {

	public Double parse(String value) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new VariableParseException("The given value \"" + value + "\" cannot be parsed into a double.");
		}
	}

}
