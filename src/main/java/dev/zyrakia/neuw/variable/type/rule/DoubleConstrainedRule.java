package dev.zyrakia.neuw.variable.type.rule;

/**
 * This rule passes doubles constrained by a minimum and maximum value.
 */
public class DoubleConstrainedRule implements VariableTypeRule<Double> {

    /**
     * Represents the minimum double that is passed by this rule.
     */
    private final double min;

    /**
     * Represents the maximum double that is passed by this rule.
     */
    private final double max;

    /**
     * Creates a new constrained rule with the given min and max.
     *
     * @param min the minimum double that can pass with this rule
     * @param max the maximum double that can pass with this rule
     */
    public DoubleConstrainedRule(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Creates a new constrained rule that is only constrained with a max.
     *
     * @param max the maximum double that can pass with this rule
     */
    public DoubleConstrainedRule(double max) {
        this(Double.MIN_VALUE, max);
    }

    @Override
    public boolean validate(Double value) {
        return value >= this.min && value <= this.max;
    }

    @Override
    public String toString() {
        return "a double between " + this.min + " and " + this.max;
    }

}
