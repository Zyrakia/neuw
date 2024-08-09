package dev.zyrakia.neuw.app.pkg;

import static org.jline.jansi.Ansi.ansi;

import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import org.jline.jansi.Ansi.Color;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;

import dev.zyrakia.neuw.app.TerminalApp;

/**
 * This package prompts the user for a boolean response.
 */
public class ConfirmPromptPackage implements TerminalPackage<Boolean> {

    /**
     * The list of words that resolve as a confirm. (Should all be lowercase)
     */
    private static final List<String> CONFIRM_WORDS = List
            .of("yes", "y", "confirm");

    /**
     * The list of words that resolve as a deny. (Should all be lowercase)
     */
    private static final List<String> DENY_WORDS = List.of("no", "n", "deny");

    /**
     * The completer that compiles the confirm and deny words into completion
     * categories.
     */
    private static final Completer WORD_COMPLETER = (__, ___, list) -> {
        list.addAll(CONFIRM_WORDS.stream()
                .map((v) -> new Candidate(v, v, "Confirm", "Indicates the confirmation of this prompt.", "Deny", "Confirm", true, 0))
                .collect(Collectors.toList()));
        list.addAll(DENY_WORDS.stream()
                .map((v) -> new Candidate(v, v, "Deny", "Indicates the denial of this prompt.", "Deny", "Deny", true, 0))
                .collect(Collectors.toList()));
    };

    /**
     * The prompt that is given to the user when this packge is executed.
     */
    private final String prompt;

    /**
     * The default value for each displayed prompt.
     */
    private final boolean def;

    public ConfirmPromptPackage(String prompt, boolean def) {
        this.prompt = prompt;
        this.def = def;
    }

    @Override
    public Boolean execute(TerminalApp app) {
        LineReader reader = app.newReader()
                .completer(WORD_COMPLETER)
                .option(LineReader.Option.INSERT_TAB, false)
                .option(LineReader.Option.COMPLETE_IN_WORD, true)
                .option(LineReader.Option.CASE_INSENSITIVE, true)
                .option(LineReader.Option.GROUP_PERSIST, true)
                .option(LineReader.Option.AUTO_FRESH_LINE, true)
                .option(LineReader.Option.ERASE_LINE_ON_FINISH, true)
                .build();

        boolean res = this.def;
        while (true) {
            this.writePrompt(app);
            String in = reader.readLine().trim().toLowerCase();
            app.clearLastLine();

            if (in.isBlank()) res = def;
            else if (CONFIRM_WORDS.contains(in)) res = true;
            else if (DENY_WORDS.contains(in)) res = false;
            else continue;

            break;
        }

        this.writeResult(app, res);
        return res;
    }

    /**
     * Returns the styled prompt prefix.
     * 
     * @return the prompt prefix
     */
    private String getPromptPrefix() {
        return ansi().bold()
                .fg(Color.BLUE)
                .a("? ")
                .reset()
                .a(this.prompt + " ")
                .toString();
    }

    /**
     * Writes the prompt to the terminal, highlighting the default value for
     * this package. This does not write a newline.
     * 
     * @param app the app to write the prompt to
     */
    private void writePrompt(TerminalApp app) {
        PrintWriter writer = app.writer();

        String choiceIndicator = (this.def
                ? ansi().bold().fg(Color.GREEN).a("Y").reset().a("/n")
                : ansi().a("y/").bold().fg(Color.RED).a("N")).reset()
                        .toString();

        writer.print(ansi().a(this.getPromptPrefix())
                .fg(Color.BLUE)
                .a("[")
                .a(choiceIndicator)
                .fg(Color.BLUE)
                .a("] ")
                .reset());
        writer.flush();
    }

    /**
     * Writes the given result, alongside the prompt, to the terminal.
     * 
     * @param app the app to write the result to
     * @param res the result of the prompt
     */
    private void writeResult(TerminalApp app, boolean res) {
        PrintWriter writer = app.writer();
        String decisionIndicator = (res ? ansi().bold().fg(Color.GREEN).a("Yes")
                : ansi().bold().fg(Color.RED).a("No")).reset().toString();

        writer.println(ansi().a(this.getPromptPrefix()).a(decisionIndicator));
        writer.flush();
    }

}
