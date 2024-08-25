package dev.zyrakia.neuw.app.pkg;

import static org.jline.jansi.Ansi.ansi;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;

import org.jline.jansi.Ansi;
import org.jline.jansi.Ansi.Color;

import dev.zyrakia.neuw.app.TerminalApp;
import dev.zyrakia.neuw.structure.WriteResult;

public class WriteResultLoggerPackage implements TerminalPackage<Void> {

    /**
     * Represents the result that this package will log when executed.
     */
    private final List<WriteResult> results;

    /**
     * Represents the optional root path that will be used to relativize the
     * paths of the results.
     */
    private final Path rootPath;

    /**
     * Creates a new package that will log the given results when executed.
     * 
     * @param results the results to log
     * @param rootPath the root path to relativize all entries with
     */
    public WriteResultLoggerPackage(List<WriteResult> results, Path rootPath) {
        this.results = results;
        this.rootPath = rootPath;
    }

    /**
     * Creates a new package that will log the given results when executed.
     * 
     * @param results the results to log
     * @return the created package
     */
    public WriteResultLoggerPackage(List<WriteResult> results) {
        this(results, null);
    }

    /**
     * Generates an informational message for the given {@link WriteResult}.
     * 
     * @param res the result to generate the message for
     * @return the message
     */
    private static String generateMessage(WriteResult res) {
        Ansi message = ansi();

        if (!res.success()) {
            message = message.fgRed()
                    .a("there was an error while writing this item");
        } else {
            if (res.contentWritten()) {
                if (res.overwritten()) message = message.fgYellow()
                        .a("an existing file was overwritten");
            } else {
                if (res.item().isDirectory()) {
                    if (res.overwritten()) {
                        message = message.fgYellow()
                                .a("an existing directory was overwritten");
                    }
                } else {
                    if (res.overwritten()) {
                        message = message.fgRed()
                                .a("no content was found, so a blank file overwrote an existing file");
                    } else {
                        message = message.fgYellow().a("no content was found");
                    }
                }
            }
        }

        return message.toString();
    }

    @Override
    public Void execute(TerminalApp app) {
        PrintWriter writer = app.writer();
        writer.println(ansi().newline()
                .fg(Color.CYAN)
                .a("Structure write results: ")
                .reset());

        app.writeSeperator();

        for (WriteResult res : this.results) {
            Path path = this.rootPath == null ? res.absPath()
                    : rootPath.relativize(res.absPath());
            String fileName = " ".repeat(4)
                    .repeat(Math.max(0, path.getNameCount() - 1))
                    .concat(path.getFileName().toString())
                    .concat(res.item().isDirectory()
                            ? ansi().bold()
                                    .fg(Color.CYAN)
                                    .a("/")
                                    .reset()
                                    .toString()
                            : "");

            String msg = WriteResultLoggerPackage.generateMessage(res);
            writer.println(msg.toString().isBlank()
                    ? ansi().fg(Color.GREEN).a(fileName).reset()
                    : ansi().fgBright(Color.RED)
                            .a(fileName)
                            .reset()
                            .a(" - ")
                            .a(msg)
                            .reset());
        }

        app.writeSeperator();

        return null;
    }

}
