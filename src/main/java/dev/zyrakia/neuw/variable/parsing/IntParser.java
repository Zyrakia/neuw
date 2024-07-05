package dev.zyrakia.neuw.variable.parsing;

import dev.zyrakia.neuw.exception.VariableParseException;

/**
 * Responsible for parsing integer values for variables.
 */
public class IntParser implements VariableParser<Integer> {

	public Integer parse(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new VariableParseException("The given value \"" + value + "\" cannot be parsed into an integer.");
		}
	}

}
