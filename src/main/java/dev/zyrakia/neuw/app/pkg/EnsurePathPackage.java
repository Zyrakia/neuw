package dev.zyrakia.neuw.app.pkg;

import static org.jline.jansi.Ansi.ansi;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

import org.jline.jansi.Ansi.Color;

import dev.zyrakia.neuw.app.TerminalApp;

/**
 * This package ensures that a given path exists, either a file or directory.
 */
public class EnsurePathPackage implements TerminalPackage<Boolean> {

    /**
     * The type of path to ensure, this ensures that when checking for existence
     * or creation, the path is a file or directory.
     */
    public static enum EnsureType {
        FILE, DIRECTORY,
    }

    /**
     * The path to ensure.
     */
    private final Path path;

    /**
     * The type of path to ensure.
     */
    private final EnsureType type;

    /**
     * Whether to warn the user if the path already exists, if this is true and
     * the user denies access, this package will return `false`.
     */
    private final boolean warnIfExists;

    /**
     * Creates a new package to ensure that the given path exists.
     * 
     * @param path the path to ensure
     * @param type the type of path to ensure
     * @param warnIfExists whether to warn the user if the path already exists
     */
    public EnsurePathPackage(Path path, EnsureType type, boolean warnIfExists) {
        this.path = path;
        this.type = type;
        this.warnIfExists = warnIfExists;
    }

    @Override
    public Boolean execute(TerminalApp app) {
        PrintWriter writer = app.writer();

        boolean exists = this.checkExistence();
        if (exists) {
            if (this.warnIfExists) {
                writer.println(this.generateExistsWarning());
                return app
                        .run(new ConfirmPromptPackage("Do you want to continue?", true));
            } else return true;
        }

        try {
            boolean created = this.createPath();
            if (created) writer.println(this.generateCreatedMessage());
            return created;
        } catch (IOException e) {
            app.writeException(e);
            return false;
        }
    }

    /**
     * Checks whether the path exists as the type it needs to exist as.
     * 
     * @return whether the path exists as the correct type
     */
    private boolean checkExistence() {
        switch (this.type) {
        case FILE:
            return this.path.toFile().isFile();

        case DIRECTORY:
            return this.path.toFile().isDirectory();

        default:
            return false;
        }
    }

    /**
     * Creates the path as the type it needs to exist as.
     * 
     * @return whether the path was created
     * @throws IOException if the path could not be created
     */
    private boolean createPath() throws IOException {
        if (this.type == EnsureType.FILE)
            return this.path.toFile().createNewFile();
        else return this.path.toFile().mkdirs();
    }

    /**
     * Geneates a message about the path this package ensures having already
     * been found on the disk.
     * 
     * @return the generated message
     */
    private String generateExistsWarning() {
        return ansi().fg(Color.RED)
                .bold()
                .a("! ")
                .boldOff()
                .reset()
                .format("The given %s already exists: %s", this.type == EnsureType.FILE
                        ? "file"
                        : "directory", ansi().fg(Color.CYAN)
                                .a(this.path.toAbsolutePath())
                                .reset())
                .reset()
                .toString();
    }

    /**
     * Generates a message about the path this package ensures having been
     * created.
     * 
     * @return the generated message
     */
    private String generateCreatedMessage() {
        return ansi()
                .format("The given %s was created: %s", this.type == EnsureType.FILE
                        ? "file"
                        : "directory", ansi().fg(Color.CYAN)
                                .a(this.path.toAbsolutePath())
                                .reset())
                .reset()
                .toString();
    }

}
