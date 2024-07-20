package dev.zyrakia.neuw.variable.type.rule;

/**
 * This rule passes strings that are of a given length, constrained with a minimum and maximum length.
 */
public class StringConstrainedRule implements VariableTypeRule<String> {

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
	public StringConstrainedRule(int min, int max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Creates a new string clamp rule with only the maximum specified.
	 *
	 * @param max the maximum string length that can pass this rule
	 */
	public StringConstrainedRule(int max) {
		this(0, max);
	}

	@Override
	public boolean validate(String value) {
		int len = value.length();
		return len >= this.min && len <= this.max;
	}

	@Override
	public String toString() {
		return "StringConstrainedRule<min = " + this.min + ", max = " + this.max + ">";
	}

}
