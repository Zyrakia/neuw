package dev.zyrakia.neuw.variable.type.rule;

/**
 * This rule passes integers constrained by a minimum and maximum value.
 */
public class IntConstrainedRule implements VariableTypeRule<Integer> {

	/**
	 * Represents the minimum integer that is passed by this rule.
	 */
	private final int min;

	/**
	 * Represents the maximum integer that is passed by this rule.
	 */
	private final int max;

	/**
	 * Creates a new constrained rule with the given min and max.
	 *
	 * @param min the minimum integer that can pass with this rule
	 * @param max the maximum integer that can pass with this rule
	 */
	public IntConstrainedRule(int min, int max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Creates a new constrained rule that is only constrained with a max.
	 *
	 * @param max the maximum integer that can pass with this rule
	 */
	public IntConstrainedRule(int max) {
		this(Integer.MIN_VALUE, max);
	}

	@Override
	public boolean validate(Integer value) {
		return value >= this.min && value <= this.max;
	}

	@Override
	public String toString() {
		return "IntConstrainedRule<min = " + this.min + ", max = " + this.max + ">";
	}

}
