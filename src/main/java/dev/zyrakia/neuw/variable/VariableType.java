package dev.zyrakia.neuw.variable;

import dev.zyrakia.neuw.exception.VariableParseException;
import dev.zyrakia.neuw.exception.VariableTypeMismatchException;
import dev.zyrakia.neuw.variable.parsing.*;
import dev.zyrakia.neuw.variable.rule.VariableTypeRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a type of variable that can be used within a template.
 *
 * @param <T> the type that this variable type represents
 */
public class VariableType<T> {

	/**
	 * The base type that this variable type represents.
	 */
	private final Class<T> baseType;

	/**
	 * The parser that parses arbitrary skins into the type represented by this variable type.
	 */
	private final VariableParser<T> parser;

	/**
	 * The rules that describe this variable type.
	 */
	private List<VariableTypeRule<T>> rules = new ArrayList<>();

	/**
	 * Creates a new variable type with the given base type.
	 *
	 * @param baseType the base type that this variable type represents
	 */
	public VariableType(Class<T> baseType, VariableParser<T> parser) {
		this.baseType = baseType;
		this.parser = parser;
	}

	/**
	 * Helper method to avoid having to declare a generic type while instantiating.
	 *
	 * @param baseType  the base type that this variable type represents
	 * @param initRules the initial rules to create this variable type with
	 * @param <T>       the type that this variable type represents
	 * @return the variable type created
	 */
	@SafeVarargs
	public static <T> VariableType<T> of(Class<T> baseType, VariableParser<T> parser,
	                                     VariableTypeRule<T>... initRules) {
		return new VariableType<>(baseType, parser).addRule(initRules);
	}

	/**
	 * Creates a new variable type that matches strings.
	 *
	 * @return a string variable type
	 */
	public static VariableType<String> string() {
		return VariableType.of(String.class, new StringParser());
	}

	/**
	 * Creates a new variable type that matches integers.
	 *
	 * @return an integer variable type
	 */
	public static VariableType<Integer> integer() {
		return VariableType.of(Integer.class, new IntParser());
	}

	/**
	 * Creates a new variable type that matches doubles.
	 *
	 * @return a double variable type
	 */
	public static VariableType<Double> dbl() {
		return VariableType.of(Double.class, new DoubleParser());
	}

	/**
	 * Creates a new variable type that matches booleans.
	 *
	 * @return a boolean variable type
	 */
	public static VariableType<Boolean> bool() {
		return VariableType.of(Boolean.class, new BoolParser());
	}

	/**
	 * Returns whether the given value matches the type that this variable type represents, including all rules.
	 *
	 * @param value the value to test
	 * @return whether the value matches this type
	 */
	public boolean validate(Object value) {
		try {
			this.assertValidate(value);
			return true;
		} catch (VariableTypeMismatchException e) {
			return false;
		}
	}

	/**
	 * Asserts that the given value matches the type that this variable type represents, including all rules.
	 *
	 * @param value the value to assert
	 * @throws VariableTypeMismatchException if the given value does not match
	 */
	public void assertValidate(Object value) throws VariableTypeMismatchException {
		if (!this.baseType.isAssignableFrom(value.getClass())) throw new VariableTypeMismatchException(
				"The given value \"" + value + "\" does not match the given base type: " + this.baseType.getCanonicalName() + ".");

		T casted = this.baseType.cast(value);
		this.assertValidateRules(casted);
	}

	/**
	 * Asserts that the given value follows all the rules of this variable type.
	 *
	 * @param value the value to test
	 * @throws VariableTypeMismatchException if the given value does not follow one of the rules
	 */
	public void assertValidateRules(T value) {
		for (VariableTypeRule<T> rule : this.rules) {
			if (rule.validate(value)) continue;
			throw new VariableTypeMismatchException(
					"The value \"" + value + "\" does not follow the given rule: " + rule + ".");
		}
	}

	/**
	 * Adds the given rules to this variable type.
	 *
	 * @param rules the rules to add
	 * @return this variable type instance
	 */
	@SafeVarargs
	public final VariableType<T> addRule(VariableTypeRule<T>... rules) {
		this.rules.addAll(Arrays.asList(rules));
		return this;
	}

	/**
	 * Parses the give value into the base type that this variable type represents.
	 *
	 * @param value the value to parse
	 * @return the parsed value
	 * @throws VariableParseException if the given value cannot be parsed into this variables base type
	 */
	public T parseBaseType(String value) throws VariableParseException {
		return this.parser.parse(value);
	}

	/**
	 * Gets the base type that this variable represents.
	 *
	 * @return the base type
	 */
	public Class<T> getBaseType() {
		return this.baseType;
	}

}
