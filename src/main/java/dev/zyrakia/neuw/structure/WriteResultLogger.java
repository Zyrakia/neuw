package dev.zyrakia.neuw.structure;

import static org.jline.jansi.Ansi.ansi;

import java.nio.file.Path;
import java.util.List;

import org.jline.jansi.Ansi;

/**
 * This class is responsible for writing a write result in a neat manner.
 */
public class WriteResultLogger {

    /**
     * Represents the results that this writer acts upon.
     */
    private final List<WriteResult> results;

    /**
     * Creates a new writer with the given results.
     * 
     * @param results all the results to write
     */
    public WriteResultLogger(List<WriteResult> results) {
        this.results = results;
    }

    /**
     * Logs each of the results within this logger in a tree format. This assumes
     * that the results this logger has are sorted.
     * 
     * @param rootPath the root path to relativize all entries with
     */
    public void logTree(Path rootPath) {
        for (WriteResult res : this.results) {
            Path relativePath = rootPath.relativize(res.absPath());
            String fileName = " ".repeat(2).repeat(Math.max(0, relativePath.getNameCount() - 1))
                    .concat(relativePath.getFileName().toString()).concat(res.item().isDirectory() ? "/" : "");

            Ansi msg = WriteResultLogger.generateMessage(res);
            System.out.println(msg.toString().isBlank() ? fileName : fileName + " - " + msg.reset());
        }
    }

    /**
     * Generates an informational message for the given {@link WriteResult}.
     * 
     * @param res the result to generate the message for
     * @return the message
     */
    private static Ansi generateMessage(WriteResult res) {
        Ansi message = ansi();

        if (!res.success()) {
            message = message.fgRed().a("there was an error while writing this item");
        } else {
            if (res.contentWritten()) {
                if (res.overwritten())
                    message = message.fgYellow().a("an existing file was overwritten");
            } else {
                if (res.item().isDirectory()) {
                    if (res.overwritten()) {
                        message = message.fgYellow().a("an existing directory was overwritten");
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
        }

        return message;
    }

}
