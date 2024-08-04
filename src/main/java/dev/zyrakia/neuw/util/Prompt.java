package dev.zyrakia.neuw.util;

import static org.jline.jansi.Ansi.ansi;

import java.util.Scanner;

import org.jline.jansi.Ansi;
import org.jline.jansi.Ansi.Color;

/**
 * Utility class for simple terminal prompts.
 */
public class Prompt {

    /**
     * The scanner used for all prompts, hooked to `System.in`, this should not be
     * closed.
     */
    private static final Scanner SCAN = new Scanner(System.in);

    /**
     * Prompts with the given string, for a yes or no answer.
     * 
     * @param prompt the string prompt
     * @param def    the default value
     * @return the chosen value
     */
    public static boolean yayNay(String prompt, boolean def) {
        boolean res = def;

        while (true) {
            System.out.print(Prompt.buildYayNayPrompt(prompt, res));
            String in = SCAN.nextLine().trim().toLowerCase();
            System.out.print(ansi().cursorUpLine().cursorLeft(999).eraseLine().reset());

            if (in.isBlank())
                res = def;
            else if (in.equals("y") || in.equals("yes"))
                res = true;
            else if (in.equals("n") || in.equals("no"))
                res = false;
            else
                continue;

            System.out.print(Prompt.buildYayNayPrompt(prompt, res));
            break;
        }

        System.out.println();
        return res;
    }

    /**
     * Creates a yay/nay choice string with the given option highlighted.
     * 
     * @param prompt      the string prompt of the yay/nay choice
     * @param highlighted the highlighted option
     * @returns the built choice string
     */
    private static String buildYayNayPrompt(String prompt, boolean highlighted) {
        Ansi decision = highlighted ? ansi().a("[").bold().fg(Color.GREEN).a("Y").reset().a("/n]")
                : ansi().a("[y/").bold().fg(Color.RED).a("N").reset().a("]").reset();

        return ansi().bold().fg(Color.BLUE).a("? ").reset().a(prompt).a(" ").a(decision).a(" ").reset().toString();
    }

}
