package dev.zyrakia.neuw.variable.type.rule;

import dev.zyrakia.neuw.construction.PropertyCreatable;

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

	public IntConstrainedRule() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Creates a new constrained rule with the given min and max.
	 *
	 * @param min the minimum integer that can pass with this rule
	 * @param max the maximum integer that can pass with this rule
	 */
	@PropertyCreatable({ "min", "max" })
	public IntConstrainedRule(int min, int max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Creates a new constrained rule that is only constrained with a max.
	 *
	 * @param max the maximum integer that can pass with this rule
	 */
	@PropertyCreatable({ "max" })
	public IntConstrainedRule(int max) {
		this(Integer.MIN_VALUE, max);
	}

	/**
	 * Creates a new constrained rule that is only constrained with a min.
	 * 
	 * @param min the minimum integer that can pass with this rule
	 */
	@PropertyCreatable({ "min" })
	public IntConstrainedRule(Integer min) {
		this(min, Integer.MAX_VALUE);
	}

	@Override
	public boolean validate(Integer value) {
		return value >= this.min && value <= this.max;
	}

	@Override
	public String toString() {
		return "an integer between " + this.min + " and " + this.max;
	}

}
