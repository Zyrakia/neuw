package dev.zyrakia.neuw.variable.type.rule;

import java.util.regex.Pattern;

/**
 * This rule passes strings that are in a specified set of allowed strings.
 */
public class StringEnumRule extends StringPatternRule {

	/**
	 * Creates a new enum rule with the given enum items.
	 *
	 * @param caseSensitive whether the validation is case-sensitive
	 * @param enumItems     the items to validate input against
	 */
	public StringEnumRule(boolean caseSensitive, String... enumItems) {
		super(caseSensitive ? Pattern.compile(buildPatternText(enumItems)) : Pattern.compile(
				buildPatternText(enumItems), Pattern.CASE_INSENSITIVE));
	}

	/**
	 * Creates a new case-insensitive enum rule with the given enum items.
	 *
	 * @param enumItems the items to validate input against
	 */
	public StringEnumRule(String... enumItems) {
		this(false, enumItems);
	}

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

}
