package dev.zyrakia.neuw.shell;

import java.util.HashMap;

/**
 * This class is used to create evaluations with a provided scope.
 */
public interface EvaluationShellFactory {


	/**
	 * Creates a new evaluation shell with the given variables.
	 *
	 * @param variables the variables that should be available to expression evaluated by this shell
	 * @return the created shell
	 */
	EvaluationShell make(HashMap<String, Object> variables);

	/**
	 * Creates a new evaluation shell.
	 *
	 * @return the created shell
	 */
	default EvaluationShell make() {
		return make(new HashMap<>());
	}
}
