package dev.zyrakia.neuw.evaluation.rhino;

import dev.zyrakia.neuw.evaluation.SourcedEvaluationShell;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * An evaluation shell that evaluates Javascript with the Rhino engine.
 */
public class RhinoEvaluationShell implements SourcedEvaluationShell {

	/**
	 * Represents the scope that expressions will be evaluated in.
	 */
	private final ScriptableObject sharedScope;

	/**
	 * Creates a new evaluation shell with the given scope.
	 * <p>
	 * Each evaluated expression will be running within this scope.
	 *
	 * @param sharedScope the shared scope of the shell
	 */
	public RhinoEvaluationShell(ScriptableObject sharedScope) {
		this.sharedScope = sharedScope;
	}

	public Object evaluate(String expression, String source) {
		try (Context cx = Context.enter()) {
			cx.setOptimizationLevel(-1);

			Scriptable expressionScope = cx.newObject(this.sharedScope);
			expressionScope.setPrototype(this.sharedScope);
			expressionScope.setParentScope(null);

			return cx.evaluateString(expressionScope, expression, source, 0, null);
		}
	}

}
