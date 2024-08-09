package dev.zyrakia.neuw.app.pkg;

import static org.jline.jansi.Ansi.ansi;

import java.io.PrintWriter;
import java.util.stream.Collectors;

import org.jline.jansi.Ansi.Color;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;

import dev.zyrakia.neuw.app.TerminalApp;
import dev.zyrakia.neuw.exception.ValidationException;
import dev.zyrakia.neuw.exception.VariableFormatException;
import dev.zyrakia.neuw.variable.Variable;
import dev.zyrakia.neuw.variable.type.BoolVariableType;
import dev.zyrakia.neuw.variable.type.VariableType;

/**
 * This package prompts a user for the value for the supplied variable.
 */
public class VariablePromptPackage<T> implements TerminalPackage<T> {

    /**
     * Represents the variable that this package prompts for.
     */
    private final Variable<T> var;

    /**
     * Represents the completer used to provide options when prompting for a
     * variable value.
     */
    private final Completer completer;

    /**
     * Creates a new package that prompts for a value for the given variable/
     * 
     * @param var the variable to prompt for
     */
    public VariablePromptPackage(Variable<T> var) {
        this.var = var;
        this.completer = buildCompleter(var);
    }

    @Override
    public T execute(TerminalApp app) {
        PrintWriter writer = app.writer();

        writer.println(this.getHeader());
        app.writeSeperator(Color.RED);

        LineReader reader = app.newReader()
                .completer(this.completer)
                .option(LineReader.Option.INSERT_TAB, false)
                .option(LineReader.Option.COMPLETE_IN_WORD, true)
                .option(LineReader.Option.CASE_INSENSITIVE, false)
                .option(LineReader.Option.GROUP_PERSIST, true)
                .build();

        while (true) {
            String input = reader.readLine(this.getPrompt());
            if (input.isEmpty()) {
                if (this.var.required() && this.var.defaultValue() == null)
                    continue;
                else break;
            }

            try {
                T value = this.var.type().parse(input);
                var.type().validate(value).assertIsValid();
                return value;
            } catch (VariableFormatException | ValidationException e) {
                app.writeException(e);
            }
        }

        return this.var.defaultValue();
    }

    /**
     * Generates a header for the variable of this package.
     */
    private String getHeader() {
        StringBuilder header = new StringBuilder();

        header.append(ansi().fg(Color.CYAN)
                .a("$ ")
                .fg(Color.DEFAULT)
                .a(var.name().isEmpty() ? var.identifier() : var.name())
                .a(" - ")
                .a(var.description().isEmpty()
                        ? ansi().fg(Color.RED)
                                .a("no description provided.")
                                .reset()
                        : var.description())
                .reset()
                .newline());

        header.append(ansi().a("Required: ")
                .a(var.required() ? ansi().fg(Color.RED).a("true")
                        : ansi().fg(Color.GREEN).a("false"))
                .reset());

        header.append(ansi().bold().fg(Color.CYAN).a(" • ").reset());

        header.append(ansi().a("Default: ")
                .a(var.defaultValue() == null ? ansi().fg(Color.RED).a("null")
                        : ansi().fg(Color.GREEN).a(var.defaultValue()))
                .reset());

        return header.toString();
    }

    /**
     * Generates a prompt for the variable of this package.
     */
    private String getPrompt() {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Enter a value");
        if (var.defaultValue() != null)
            prompt.append(" (enter to use default)");
        else if (!var.required()) prompt.append(" (enter to skip)");
        prompt.append(": ");

        return prompt.toString();
    }

    /**
     * Builds a completer based on some default variable types to provide useful
     * suggestions when filling in variable values.
     * 
     * @param var the variable to build the completer for
     * @return the generated completer
     */
    private static Completer buildCompleter(Variable<?> var) {
        VariableType<?> type = var.type();

        return (__, ___, list) -> {
            if (type instanceof BoolVariableType boolType) {
                list.addAll(boolType.getTrueWords()
                        .stream()
                        .map((v) -> new Candidate(v, v, "True", "Indicates a value of \"true\"", "True", "True", true, 0))
                        .collect(Collectors.toList()));

                list.addAll(boolType.getFalseWords()
                        .stream()
                        .map((v) -> new Candidate(v, v, "False", "Indicates a value of \"false\"", "False", "False", true, 0))
                        .collect(Collectors.toList()));
            }
        };
    }

}
