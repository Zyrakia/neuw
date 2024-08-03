package dev.zyrakia.neuw.variable.ctx.population;

import static org.jline.jansi.Ansi.ansi;

import java.io.IOException;
import java.io.PrintWriter;

import org.jline.jansi.Ansi.Color;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;

import dev.zyrakia.neuw.exception.ValidationException;
import dev.zyrakia.neuw.exception.VariableFormatException;
import dev.zyrakia.neuw.variable.Variable;

/**
 * This populator is used to prompt the user for each variable that is given to
 * it.
 */
public class PromptPopulator implements ContextPopulator {

    /**
     * The terminal instance that this populator uses.
     */
    private Terminal terminal;

    /**
     * Creates a new prompt populator. This creates a system terminal.
     * 
     * @throws IOException if the system terminal creation failed
     */
    public PromptPopulator() throws IOException {
        this.terminal = TerminalBuilder.builder().system(true).build();
    }

    @Override
    public <T> T populate(Variable<T> var) {
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

        this.enterAlternateScreen();
        try {
            this.clearScreen();
            this.writeHeader(var);

            while (true) {
                String input = this.promptFor(var, reader);
                if (input == null) {
                    if (var.required())
                        continue;
                    else
                        break;
                }

                try {
                    T value = var.type().parse(input);
                    var.type().validate(value).assertIsValid();
                    return value;
                } catch (VariableFormatException | ValidationException e) {
                    this.writeException(e);
                }
            }
        } finally {
            this.exitAlternateScreen();
        }

        return null;
    }

    /**
     * Prints a prompt with the given line reader that asks for the value for a
     * given variable.
     * 
     * @param var    the variable to prompt for
     * @param reader the reader to prompt with
     * @return the input that was given, or null if nothing was provided
     */
    private String promptFor(Variable<?> var, LineReader reader) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Enter a value");
        if (var.defaultValue() != null)
            prompt.append(" (enter to use default)");
        else if (!var.required())
            prompt.append(" (enter to skip)");
        prompt.append(": ");

        String input = reader.readLine(prompt.toString());
        if (input.isEmpty())
            return null;
        return input;
    }

    /**
     * Writes a header for the given variable, including the name, description and
     * certain attributes.
     * 
     * @param var the variable to print the header for
     */
    private void writeHeader(Variable<?> var) {
        PrintWriter writer = this.terminal.writer();

        writer.println(
                ansi().bold().fgRgb(255, 127, 80).a(this.center("Neuw - Instantiate Template Variables"))
                        .reset());

        this.writeSeparator();

        writer.println(
                ansi().fg(Color.CYAN).a("$ ").fg(Color.DEFAULT).a(var.name().isEmpty() ? var.identifier() : var.name())
                        .a(" - ")
                        .a(var.description().isEmpty() ? ansi().fg(Color.RED).a("no description provided.").reset()
                                : var.description())
                        .reset());
        writer.print(ansi().a("Required: ")
                .a(var.required() ? ansi().fg(Color.RED).a("true") : ansi().fg(Color.GREEN).a("false")).reset());
        writer.print(ansi().bold().fg(Color.CYAN).a(" • ").reset());
        writer.println(ansi().a("Default: ").a(var.defaultValue() == null ? ansi().fg(Color.RED).a("null")
                : ansi().fg(Color.GREEN).a(var.defaultValue())).reset());

        this.writeSeparator();

        writer.flush();
    }

    /**
     * Writes the given exception, formatted to be more readable, without a trace.
     * 
     * @param e the exception to print
     */
    private void writeException(Exception e) {
        PrintWriter writer = this.terminal.writer();
        writer.println(
                ansi().bold().fg(Color.RED).a(e.getClass().getSimpleName()).boldOff().a(" - ")
                        .a(e.getLocalizedMessage())
                        .reset());
    }

    /**
     * Clears the terminal screen.
     */
    private void clearScreen() {
        this.terminal.puts(Capability.clear_screen);
        this.terminal.flush();
    }

    /**
     * Writes a seperator line that is the same size as the current terminal.
     */
    private void writeSeparator() {
        PrintWriter writer = this.terminal.writer();
        writer.println(ansi().bold().fg(Color.RED).a("⎯".repeat(terminal.getWidth())).reset());
        writer.flush();
    }

    /**
     * Offsets the given text so that it will be center-aligned within the current
     * terminal size.
     * 
     * @param text the text to center
     * @return the centered text
     */
    private String center(String text) {
        int padding = (this.terminal.getWidth() - text.length()) / 2;
        padding = Math.max(0, padding);
        return " ".repeat(padding).concat(text);
    }

    /**
     * Enters the alternative screen mode, saving the current terminal contents.
     */
    private void enterAlternateScreen() {
        this.terminal.puts(Capability.enter_ca_mode);
        this.terminal.puts(Capability.keypad_xmit);
        this.terminal.flush();
    }

    /**
     * Exits the alternative screen mode, restoring the previous terminal contents.
     */
    private void exitAlternateScreen() {
        terminal.puts(Capability.exit_ca_mode);
        terminal.puts(Capability.keypad_local);
        terminal.flush();
    }

}