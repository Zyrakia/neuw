package dev.zyrakia.neuw.variable.type.rule;

/**
 * Represents a rule that expands the criteria that a value must pass to match a
 * variable type.
 *
 * @param <T> the type of value this rule validates
 */
public interface VariableTypeRule<T> {

	/**
	 * Returns whether the given value follows this rule.
	 *
	 * @param value the value to check against
	 * @return true if the value follows this rule, false otherwise
	 */
	boolean validate(T value);

	/**
	 * Returns a representation of this rule that describes how this rule
	 * validates input.
	 *
	 * @return the string representation of this rule
	 */
	String toString();

}
