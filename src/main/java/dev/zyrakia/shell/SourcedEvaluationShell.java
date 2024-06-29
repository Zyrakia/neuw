package dev.zyrakia.shell;

/**
 * Represents an {@link EvaluationShell} that allows for source attachment to expressions.
 */
public interface SourcedEvaluationShell extends EvaluationShell {

	/**
	 * Evaluates the given expression with an anonymous source.
	 *
	 * @param expression the expression to evaluate
	 * @return the result of the evaluation
	 */
	@Override
	default Object evaluate(String expression) {
		return evaluate(expression, "<anon>");
	}

	/**
	 * Evaluates the given expression with the given source.
	 *
	 * @param expression the expression to evaluate
	 * @param source     the source of the expression
	 * @return the result of the evaluation
	 */
	Object evaluate(String expression, String source);

}
