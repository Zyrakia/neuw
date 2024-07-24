package dev.zyrakia.neuw.variable.ctx.population;

import dev.zyrakia.neuw.variable.Variable;
import dev.zyrakia.neuw.variable.ctx.VariableContext;

/**
 * This is used to provide a value for arbitrary variables, such as prompting
 * the user or querying the identifier in a map. It is used to populate the
 * instances within a {@link VariableContext} in bulk.
 */
public interface ContextPopulator {

    /**
     * Returns the value that should be placed into an instance of the given
     * variable.
     * 
     * @param var the variable to populate
     * @returns the value that should be placed into an instance of the variable
     */
    public <T> T populate(Variable<T> var);

}
