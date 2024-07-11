package dev.zyrakia.neuw.variable;

import dev.zyrakia.neuw.exception.UnsetRequiredVariableException;
import dev.zyrakia.neuw.exception.VariableParseException;
import dev.zyrakia.neuw.exception.VariableTypeMismatchException;

/**
 * Represents a user-specified variable in a project template.
 *
 * @param <T> the type that this variable represents
 */
public class Variable<T> {

	/**
	 * Represents the type of this variable.
	 */
	private final VariableType<T> type;

	/**
	 * Represents the name of the variable.
	 */
	private String name;

	/**
	 * Represents the description of the variable.
	 */
	private String description;

	/**
	 * Represents whether this variable is required or not.
	 */
	private boolean required;

	/**
	 * Represents the manually set value of this variable.
	 */
	private T value;

	/**
	 * Creates a new variable with the given properties.
	 *
	 * @param type        the type of the variable
	 * @param name        the name of the variable
	 * @param description the description of the variable
	 * @param required    whether the variable is required
	 */
	private Variable(VariableType<T> type, String name, String description, boolean required) {
		this.type = type;
		this.name = name;
		this.description = description;
		this.required = required;
	}

	/**
	 * Creates a new variable that has no meta, is not required.
	 *
	 * @param type the type of the variable
	 */
	private Variable(VariableType<T> type) {
		this(type, "", "", false);
	}

	/**
	 * Evaluates the current value of this variable.
	 *
	 * @return the value of this variable, or null if no value exists
	 * @throws UnsetRequiredVariableException if this variable is required but no value is set
	 * @throws VariableTypeMismatchException  if the value of this variable does not match the type this variable holds
	 */
	public T evaluate() throws UnsetRequiredVariableException, VariableTypeMismatchException {
		if (this.value == null) {
			if (this.isRequired()) throw new UnsetRequiredVariableException();
			else return null;
		}

		this.type.assertValidate(this.value);
		return this.value;
	}

	/**
	 * Sets the current internal value of this variable.
	 * <p>
	 * This will set the value without validating it against the type of this variable.
	 *
	 * @param value the new value of this variable
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * Sets the internal value of this variable by parsing the given string with the type of this variable. This does
	 * not perform any validation to the parsed value, so it may not end up passing the rules that this variable type
	 * has.
	 *
	 * @param rawValue the string to parse into the new value of this variable
	 * @throws VariableParseException if the given string cannot be parsed into a valid value
	 */
	public void setValueFrom(String rawValue) throws VariableParseException {
		this.value = this.type.parseBaseType(rawValue);
	}

	/**
	 * Returns the name of this variable.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the type of this variable.
	 *
	 * @return the type
	 */
	public VariableType<T> getType() {
		return this.type;
	}

	/**
	 * Returns the description of this variable.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the required status of this variable.
	 *
	 * @return whether this variable is required
	 */
	public boolean isRequired() {
		return this.required;
	}

	/**
	 * A utility class to create a variable and modify its properties with a builder pattern.
	 *
	 * @param <T> the type that this variable represents
	 */
	public static class New<T> {

		/**
		 * The variable that this builder is acting upon.
		 */
		private final Variable<T> var;

		/**
		 * Creates a new builder, creating a variable of the given type.
		 *
		 * @param type the type of the variable to build
		 */
		public New(VariableType<T> type) {
			this.var = new Variable<>(type);
		}

		/**
		 * Sets the default (initial) value of the variable that this builder is acting upon.
		 *
		 * @param defaultValue the default value to set
		 * @return this builder
		 */
		public New<T> withDefault(T defaultValue) {
			this.var.value = defaultValue;
			return this;
		}

		/**
		 * Sets the name of the variable that this builder is acting upon.
		 *
		 * @param name the name to set
		 * @return this builder
		 */
		public New<T> withName(String name) {
			this.var.name = name;
			return this;
		}

		/**
		 * Sets the description of the variable that this builder is acting upon.
		 *
		 * @param description the description to set
		 * @return this builder
		 */
		public New<T> withDescription(String description) {
			this.var.description = description;
			return this;
		}

		/**
		 * Sets the required status of the variable that this builder is acting upon.
		 *
		 * @param required the required status to set
		 * @return this builder
		 */
		public New<T> required(boolean required) {
			this.var.required = required;
			return this;
		}

		/**
		 * Sets the variable that this builder is acting upon as required.
		 *
		 * @return this builder
		 */
		public New<T> required() {
			return this.required(true);
		}

		/**
		 * Returns the internal variable that this builder is acting upon.
		 *
		 * @return the variable
		 */
		public Variable<T> get() {
			return this.var;
		}

	}

}
