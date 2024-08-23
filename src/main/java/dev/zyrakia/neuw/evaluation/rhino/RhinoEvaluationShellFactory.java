package dev.zyrakia.neuw.evaluation.rhino;

import dev.zyrakia.neuw.evaluation.EvaluationShellFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import java.util.Map;

/**
 * This is a factory for the {@link RhinoEvaluationShell}. It will create a
 * secure scope which does not allow accessing anything of the host application
 * unless it is provided.
 */
public class RhinoEvaluationShellFactory implements EvaluationShellFactory {

	/**
	 * Creates a new {@link RhinoEvaluationShell} with the given variables
	 * available to expressions evaluated with the resulting shell.
	 *
	 * @param variables the variables that should be available to expression
	 * evaluated by this shell
	 * @return the created shell
	 */
	@Override
	public RhinoEvaluationShell createShell(Map<String, Object> variables) {
		Context cx = Context.enter();

		ScriptableObject scope = cx.initSafeStandardObjects(null, true);
		variables.forEach((key, value) -> ScriptableObject
				.putConstProperty(scope, key, value));

		scope.sealObject();
		cx.close();
		return new RhinoEvaluationShell(scope);
	}

}
