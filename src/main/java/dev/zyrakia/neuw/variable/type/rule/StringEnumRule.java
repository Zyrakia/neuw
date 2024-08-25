package dev.zyrakia.neuw.variable.type.rule;

import java.util.regex.Pattern;

/**
 * This rule passes strings that are in a specified set of allowed strings.
 */
public class StringEnumRule extends StringPatternRule {

	/**
	 * A reference to the enum items that this rule validates against.
	 */
	private final String[] enumItems;

	/**
	 * Whether the validation is case-sensitive.
	 */
	private final boolean caseSensitive;

	/**
	 * Creates a new enum rule with the given enum items.
	 *
	 * @param caseSensitive whether the validation is case-sensitive
	 * @param enumItems the items to validate input against
	 */
	public StringEnumRule(boolean caseSensitive, String... enumItems) {
		super(caseSensitive ? Pattern.compile(buildPatternText(enumItems))
				: Pattern
						.compile(buildPatternText(enumItems), Pattern.CASE_INSENSITIVE));

		this.caseSensitive = caseSensitive;
		this.enumItems = enumItems;
	}

	/**
	 * Creates a new case-insensitive enum rule with the given enum items.
	 *
	 * @param enumItems the items to validate input against
	 */
	public StringEnumRule(String... enumItems) {
		this(false, enumItems);
	}

	/**
	 * Creates a regular expression that matches any of the given enum items.
	 * This does not account for the enum items containig regex special
	 * characters.
	 * 
	 * @param enumItems the items to add to the expression
	 * @return the regular expression
	 */
	private static String buildPatternText(String... enumItems) {
		StringBuilder patternText = new StringBuilder();

		patternText.append("^(?:");
		for (int i = 0; i < enumItems.length; i++) {
			String item = enumItems[i];
			patternText.append(item);
			if (i != enumItems.length - 1) patternText.append("|");
		}
		patternText.append(")$");

		return patternText.toString();
	}

	@Override
	public String toString() {
		return "a string equal to any of the following " + "(case "
				+ (this.caseSensitive ? "" : "in") + "sensitive)" + ": "
				+ String.join(", ", this.enumItems);
	}

}
