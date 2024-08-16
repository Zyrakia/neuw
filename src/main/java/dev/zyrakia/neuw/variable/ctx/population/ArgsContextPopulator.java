package dev.zyrakia.neuw.variable.ctx.population;

import java.util.HashMap;
import java.util.Map;

import dev.zyrakia.neuw.variable.Variable;

/**
 * This context populator populates variables from command line arguments, based
 * on the identifier of the variable.
 */
public class ArgsContextPopulator implements ContextPopulator {

    /**
     * This represents the compiled arguments, which are keyed by the identifier
     * of the variable, and the value is the raw value of the argument.
     */
    private final Map<String, String> args;

    /**
     * Creates a new args populator with the given arguments.
     * 
     * Each argument is considered to be linked to a variable, if the argument
     * is in the form of `--identifier=value`, where `identifier` is the
     * identifier of the variable, and `value` is the value of the variable.
     * 
     * @param args the arguments used to populate the variables
     */
    public ArgsContextPopulator(String[] args) {
        this.args = compileArgs(args);
    }

    @Override
    public <T> T populate(Variable<T> var) {
        if (this.args.containsKey(var.identifier())) {
            return var.type().parse(this.args.get(var.identifier()));
        } else return var.defaultValue();
    }

    /**
     * Compiles the given arguments, as provided by the Java runtime, into a map
     * of key-value pairs, where the key is the identifier of the variable, and
     * the value is the raw value of the argument.
     * 
     * @param args the arguments to compile
     * @return the compiled arguments
     */
    private static Map<String, String> compileArgs(String[] args) {
        Map<String, String> map = new HashMap<>();

        for (String arg : args) {
            String[] split = arg.split("=");

            String identifier = split[0];
            String value = split[1];

            if (!identifier.startsWith("--")) continue;
            map.put(identifier.substring(2), value);
        }

        return map;
    }

}
