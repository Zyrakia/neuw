package dev.zyrakia.neuw.variable.type;

import java.util.List;
import java.util.stream.Collectors;

import dev.zyrakia.neuw.exception.VariableFormatException;

/**
 * Represents a variable type of {@link Boolean}.
 */
public class BoolVariableType extends VariableType<Boolean> {

    /**
     * The default words that will parse into a true value.
     */
    public static final List<String> DEFAULT_TRUE_WORDS = List.of("true", "yes", "y");

    /**
     * The default words that will parse into a false value.
     */
    public static final List<String> DEFAULT_FALSE_WORDS = List.of("false", "no", "n");

    /**
     * Whether the parsing is case-sensitive.
     */
    private final boolean caseSensitive;

    /**
     * The words that will parse into a true value.
     */
    private List<String> trueWords;

    /**
     * The words that will parse into a false value.
     */
    private List<String> falseWords;

    /**
     * Creates a new boolean parser with the given true and false words.
     *
     * @param trueWords     the words that parse as {@code true}
     * @param falseWords    the words that parse as {@code false}
     * @param caseSensitive whether to take case into consideration while parsing
     */
    public BoolVariableType(List<String> trueWords, List<String> falseWords, boolean caseSensitive) {
        this.trueWords = trueWords;
        this.falseWords = falseWords;
        this.caseSensitive = caseSensitive;

        if (!caseSensitive) {
            this.trueWords = this.trueWords.stream().map(String::toLowerCase).collect(Collectors.toList());
            this.falseWords = this.falseWords.stream().map(String::toLowerCase).collect(Collectors.toList());
        }
    }

    /**
     * Creates a new, case-insensitive, boolean parser with the default true and
     * false words.
     */
    public BoolVariableType() {
        this(DEFAULT_TRUE_WORDS, DEFAULT_FALSE_WORDS, false);
    }

    public Boolean parse(String value) {
        if (!this.caseSensitive)
            value = value.toLowerCase();

        if (this.trueWords.contains(value))
            return true;
        else if (this.falseWords.contains(value))
            return false;

        throw VariableFormatException.forExpectedType(value, Boolean.class);
    }

    @Override
    public Boolean cast(Object value) throws ClassCastException {
        return Boolean.class.cast(value);
    }

    /**
     * Returns the currently accepted list of true words.
     * 
     * @return the true words list
     */
    public List<String> getTrueWords() {
        return this.trueWords;
    }

    /**
     * Returns the currently accepted list of false words.
     * 
     * @return the false words list
     */
    public List<String> getFalseWords() {
        return this.falseWords;
    }

}
