package dev.zyrakia.neuw.variable.type.rule;

import dev.zyrakia.neuw.construction.PropertyCreatable;

/**
 * This rule passes strings that are of a given length, constrained with a
 * minimum and maximum length.
 */
public class StringLengthRule implements VariableTypeRule<String> {

	/**
	 * Represents the minimum string length that can pass this rule.
	 */
	private final int min;

	/**
	 * Represents the maximum string length that can pass this rule.
	 */
	private final int max;

	/**
	 * Creates a new string clamp rule with the given minimum and maximum.
	 *
	 * @param min the minimum string length that can pass this rule
	 * @param max the maximum string length that can pass this rule
	 */
	@PropertyCreatable({ "min", "max" })
	public StringLengthRule(int min, int max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Creates a new string clamp rule that is only constrained with a maximum.
	 *
	 * @param max the maximum string length that can pass this rule
	 */
	@PropertyCreatable({ "max" })
	public StringLengthRule(int max) {
		this(0, max);
	}

	/**
	 * Creates a new string clamp rule that is only constrained with a minimum.
	 *
	 * @param min the minimum string length that can pass this rule
	 */
	@PropertyCreatable({ "min" })
	public StringLengthRule(Integer min) {
		this(min, Integer.MAX_VALUE);
	}

	@Override
	public boolean validate(String value) {
		int len = value.length();
		return len >= this.min && len <= this.max;
	}

	@Override
	public String toString() {
		return "a string with a length between " + this.min + " and "
				+ this.max;
	}

}
