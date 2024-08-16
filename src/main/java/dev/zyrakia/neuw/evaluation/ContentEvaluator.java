package dev.zyrakia.neuw.evaluation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for evaluating content in a template.
 * <p>
 * A search will be performed for any template expressions and each will be
 * evaluated if found. The result of the expression will then be used to replace
 * the text of the expression that was evaluated.
 */
public class ContentEvaluator {

	/**
	 * Represents the pattern used to search for expressions.
	 */
	Pattern expressionPattern;

	/**
	 * Represents the shell used to evaluate found expressions.
	 */
	EvaluationShell shell;

	/**
	 * Creates a new content evaluator that searches for expressions with the
	 * given pattern.
	 *
	 * @param expressionPattern the pattern used to search for expressions
	 * within content
	 * @param shell the shell used to evaluate any found expressions
	 */
	public ContentEvaluator(Pattern expressionPattern, EvaluationShell shell) {
		this.expressionPattern = expressionPattern;
		this.shell = shell;
	}

	/**
	 * Evaluates the given string, executing and replacing any found
	 * expressions.
	 *
	 * @param content the content to evaluate
	 * @param source the source of the content
	 * @return the resulting content, with all expressions evaluated
	 */
	public String evaluate(String content, String source) {
		Matcher expressionMatcher = this.expressionPattern.matcher(content);

		if (expressionMatcher.find()) {
			return expressionMatcher.replaceAll((match) -> {
				Object expressionResult = this
						.evaluateExpression(match.group(), source);
				if (expressionResult == null) return "";

				return expressionResult.toString();
			});
		} else return content;
	}

	/**
	 * Evaluates the given expression.
	 *
	 * @param expression the expression to evaluate
	 * @param source the source of the expression
	 * @return the result of the expression
	 */
	private Object evaluateExpression(String expression, String source) {
		if (this.shell instanceof SourcedEvaluationShell) {
			SourcedEvaluationShell shell = (SourcedEvaluationShell) this.shell;
			return shell.evaluate(expression, source);
		} else return this.shell.evaluate(expression);
	}

}
