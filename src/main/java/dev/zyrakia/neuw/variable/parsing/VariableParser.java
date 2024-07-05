package dev.zyrakia.neuw.variable.parsing;

import dev.zyrakia.neuw.exception.VariableParseException;

/**
 * This represents a parser that is responsible for parsing an arbitrary input string into a value for a variable.
 *
 * @param <T> the type that this parser is responsible for parsing
 */
public interface VariableParser<T> {

	/**
	 * Parses the given value into the type represented by this parser.
	 *
	 * @param value the value to parse
	 * @return the parsed value
	 * @throws VariableParseException if the given value cannot be parsed
	 */
	T parse(String value) throws VariableParseException;

}
