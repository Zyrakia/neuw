package dev.zyrakia.neuw.variable.rule;

import java.util.regex.Pattern;

/**
 * This rule passes strings that match a certain regular expression pattern.
 */
public class StringPatternRule implements VariableTypeRule<String> {

	/**
	 * Represents the pattern that will be used to validate input.
	 */
	private final Pattern pattern;

	/**
	 * Creates a new pattern rule with the given pattern.
	 *
	 * @param pattern the pattern to validate input against
	 */
	public StringPatternRule(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean validate(String value) {
		return this.pattern.matcher(value).matches();
	}

	@Override
	public String toString() {
		return "StringPatternRule<\"" + this.pattern.toString() + "\">";
	}

}
