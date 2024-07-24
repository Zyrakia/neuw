package dev.zyrakia.neuw.variable.ctx;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dev.zyrakia.neuw.exception.UnsetRequiredVariableException;
import dev.zyrakia.neuw.exception.ValidationException;
import dev.zyrakia.neuw.variable.Variable;
import dev.zyrakia.neuw.variable.ctx.population.ContextPopulator;

/**
 * This class is responsible for housing a set of variables and evaluating them
 * by their identifiers, or in bulk.
 */
public class VariableContext {

    /**
     * The descriptors of the instances that this context creates.
     */
    private final Set<Variable<?>> descriptors;

    /**
     * The variable instances currently active for each identifier.
     */
    private final Map<String, Variable<?>.Instance> instances;

    /**
     * Creates a new context that houses the given descriptors.
     * 
     * @param descriptors the variable descriptors that this context instantiates
     * @throws IllegalStateException if there are multiple descriptors with the same
     *                               identifiers
     */
    public VariableContext(Set<Variable<?>> descriptors) throws IllegalStateException {
        this.descriptors = descriptors;
        this.instances = new HashMap<>();
        this.instantiate();
    }

    /**
     * Sets the given identifier to the given value.
     * 
     * @param identifier the identifier of the variable to set
     * @param value      the value to set
     * @throws IllegalArgumentException if the given identifier does not exist in
     *                                  this variable context
     * @throws ValidationException      if the given value does not match the type
     *                                  of the variable
     */
    public void set(String identifier, Object value) throws IllegalArgumentException, ValidationException {
        Variable<?>.Instance inst = this.getInstance(identifier);
        inst.setValue(value);
    }

    /**
     * Sets all of the variables within this context with the given
     * {@link ContextPopulator}.
     * 
     * @param populator the populator to use to set each variable
     * @throws ValidationException if any of the values returned by the populator
     *                             did not match the required type of the variable
     */
    public void populate(ContextPopulator populator) throws ValidationException {
        for (Variable<?> descriptor : this.descriptors) {
            Object value = populator.populate(descriptor);
            this.getInstance(descriptor.identifier()).setValue(value);
        }
    }

    /**
     * Evaluates the values of all instantiated variables in this context.
     * 
     * @return a map of identifiers to values
     * @throws UnsetRequiredVariableException if any of the variables do not have a
     *                                        valid value set, but are required
     */
    public Map<String, Object> evaluate() throws UnsetRequiredVariableException {
        Map<String, Object> values = new HashMap<>();

        for (Map.Entry<String, Variable<?>.Instance> entry : this.instances.entrySet()) {
            String identifier = entry.getKey();
            Variable<?>.Instance inst = entry.getValue();

            values.put(identifier, inst.evaluate());
        }

        return values;
    }

    /**
     * 
     * Evaluates the current value of the variable associated with the given
     * identifier. If no valid value is set, it will return {@code null}, unless the
     * variable is required, in which case it will raise the
     * {@link UnsetRequiredVariableException}.
     * 
     * @return the current value of this variable
     * @throws UnsetRequiredVariableException if the variable is required, but no
     *                                        valid value is set
     * @throws IllegalArgumentException       if the given identifier is not
     *                                        associated with a variable
     */
    public Object evaluate(String identifier) throws UnsetRequiredVariableException, IllegalArgumentException {
        Variable<?>.Instance inst = this.getInstance(identifier);
        return inst.evaluate();
    }

    /**
     * Clears the current instances map, and instantiates all registered descriptors
     * as new instances.
     * 
     * @throws IllegalStateException if two variables with the same identifier are
     *                               instantiated
     */
    private void instantiate() throws IllegalStateException {
        this.instances.clear();

        for (Variable<?> descriptor : this.descriptors) {
            if (this.instances.containsKey(descriptor.identifier()))
                throw new IllegalStateException(
                        "The identifier \"" + descriptor.identifier() + "\" is already set.");

            instances.put(descriptor.identifier(), descriptor.instantiate());
        }
    }

    /**
     * Returns the variable instance associated with the given identifier.
     * 
     * @param identifier the identifier to get the variable instance for
     * @return the variable instance
     * @throws IllegalArgumentException if the given identifier is not associated
     *                                  with a variable
     */
    private Variable<?>.Instance getInstance(String identifier) throws IllegalArgumentException {
        if (this.instances.containsKey(identifier)) {
            return this.instances.get(identifier);
        }

        throw new IllegalArgumentException(
                "The identifier \"" + identifier + "\" does not exist in this variable context.");
    }

}
