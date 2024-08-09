package dev.zyrakia.neuw.variable.ctx.population;

import static org.jline.jansi.Ansi.ansi;

import org.jline.jansi.Ansi.Color;

import dev.zyrakia.neuw.app.TerminalApp;
import dev.zyrakia.neuw.app.pkg.VariablePromptPackage;
import dev.zyrakia.neuw.variable.Variable;

/**
 * This populator prompts a terminal app for each variable that is presented for
 * population, and then writes the populated value to the app afterwards.
 */
public class PromptContextPopulator implements ContextPopulator {

    /**
     * The app that is used to prompt for the value.
     */
    private TerminalApp app;

    /**
     * Creates a new prompt populator that prompts on the given app.
     * 
     * @param app the app to prompt on
     */
    public PromptContextPopulator(TerminalApp app) {
        this.app = app;
    }

    @Override
    public <T> T populate(Variable<T> var) {
        T value = this.app
                .runAlternate("Template Variable Declaration", new VariablePromptPackage<>(var));

        this.app.writer()
                .println(ansi().fg(Color.CYAN)
                        .a("$ ")
                        .fg(Color.DEFAULT)
                        .a(var.name().isEmpty() ? var.identifier() : var.name())
                        .a(": ")
                        .fg(value == null ? Color.RED : Color.DEFAULT)
                        .a(value)
                        .reset());

        return value;
    }

}
