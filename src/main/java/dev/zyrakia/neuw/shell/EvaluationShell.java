package dev.zyrakia.neuw.shell;

/**
 * Represents a shell that can evaluate an arbitrary expressions within templates.
 */
public interface EvaluationShell {

	/**
	 * Evaluates the given expression.
	 *
	 * @param expression the expression to evaluate
	 * @return the result of the expression
	 */
	Object evaluate(String expression);

}
