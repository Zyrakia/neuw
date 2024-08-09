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

    /**
     * Creates a new confirm prompt package with the given prompt and default
     * value.
     * 
     * @param prompt the prompt to use
     * @param def the default value of the prompt
     */
    public ConfirmPromptPackage(String prompt, boolean def) {
        this.prompt = prompt;
        this.def = def;
    }

    @Override
    public Boolean execute(TerminalApp app) {
        PrintWriter writer = app.writer();
        LineReader reader = app.newReader()
                .completer(WORD_COMPLETER)
                .option(LineReader.Option.INSERT_TAB, false)
                .option(LineReader.Option.COMPLETE_IN_WORD, true)
                .option(LineReader.Option.CASE_INSENSITIVE, true)
                .option(LineReader.Option.GROUP_PERSIST, true)
                .option(LineReader.Option.ERASE_LINE_ON_FINISH, true)
                .build();

        boolean res = this.def;
        while (true) {
            String in = reader.readLine(this.getPrompt()).trim().toLowerCase();

            if (in.isBlank()) res = def;
            else if (CONFIRM_WORDS.contains(in)) res = true;
            else if (DENY_WORDS.contains(in)) res = false;
            else continue;

            break;
        }

        writer.println(this.getResult(res));
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
     * Generates a prompt with the default value highlighted.
     * 
     * @return the generated prompt
     */
    private String getPrompt() {
        String choiceIndicator = (this.def
                ? ansi().bold().fg(Color.GREEN).a("Y").reset().a("/n")
                : ansi().a("y/").bold().fg(Color.RED).a("N")).reset()
                        .toString();

        return ansi().a(this.getPromptPrefix())
                .fg(Color.BLUE)
                .a("[")
                .a(choiceIndicator)
                .fg(Color.BLUE)
                .a("] ")
                .reset()
                .toString();
    }

    /**
     * Generaets a result for the given value, alongside the prompt.
     * 
     * @param res the result of the prompt
     * @return the generated result
     */
    private String getResult(boolean res) {
        return ansi().a(this.getPromptPrefix())
                .a((res ? ansi().bold().fg(Color.GREEN).a("Yes")
                        : ansi().bold().fg(Color.RED).a("No")).reset())
                .toString();
    }

}
