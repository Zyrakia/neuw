package dev.zyrakia.neuw.app.pkg;

import static org.jline.jansi.Ansi.ansi;

import java.io.IOException;

import org.jline.jansi.Ansi.Color;

import dev.zyrakia.neuw.app.TerminalApp;
import dev.zyrakia.neuw.cmd.ProcessRunner;

/**
 * This package runs the given process and prompts the user before continuing.
 * All output (standard or error) will be piped into the terminal app the
 * package is run on.
 */
public class ProcessRunnerPackage implements TerminalPackage<Integer> {

    /**
     * The process builder used to launch processes when this package is run.
     */
    private final ProcessBuilder pb;

    /**
     * Creates a new package with the given process builder.
     * 
     * @param pb the process builder to launch processes with
     */
    public ProcessRunnerPackage(ProcessBuilder pb) {
        this.pb = pb;
    }

    @Override
    public Integer execute(TerminalApp app) {
        try {
            ProcessRunner runner = new ProcessRunner(this.pb, (
                    v) -> pipeStandard(app, v), (v) -> pipeError(app, v));

            int exit = runner.waitFor();
            app.newLine();
            return exit;
        } catch (IOException | InterruptedException e) {
            app.writeException(e);
            return 1;
        } finally {
            app.run(new ContinuePromptPackage());
        }
    }

    /**
     * Pipes the given line into the given app as standard output.
     * 
     * @param app the app to pipe to
     * @param line the line to pipe
     */
    private static void pipeStandard(TerminalApp app, String line) {
        app.writer().println(line);
    }

    /**
     * Pipes the given line into the given app as error highlighted output.
     * 
     * @param app the app to pipe to
     * @param line the line to pipe
     */
    private static void pipeError(TerminalApp app, String line) {
        app.writer().println(ansi().fg(Color.RED).a(line).reset());
    }

}
