package dev.zyrakia.neuw.evaluation;

import java.util.Collections;
import java.util.Map;

/**
 * This class is used to create evaluations with a provided scope.
 */
public interface EvaluationShellFactory {

	/**
	 * Creates a new evaluation shell with the given variables.
	 *
	 * @param variables the variables that should be available to expression
	 * evaluated by this shell
	 * @return the created shell
	 */
	EvaluationShell make(Map<String, Object> variables);

	/**
	 * Creates a new evaluation shell.
	 *
	 * @return the created shell
	 */
	default EvaluationShell make() {
		return make(Collections.emptyMap());
	}

}
