package dev.zyrakia.neuw.app;

import static org.jline.jansi.Ansi.ansi;

import java.io.PrintWriter;

import org.jline.jansi.Ansi.Color;
import org.jline.jansi.Ansi.Erase;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Cursor;
import org.jline.terminal.Terminal;

import dev.zyrakia.neuw.app.pkg.TerminalPackage;

import static org.jline.utils.InfoCmp.Capability.*;

/**
 * Represents a terminal application that is used to communicate with the end
 * user.
 */
public class TerminalApp {

    /**
     * The underlying JLine terminal.
     */
    private final Terminal terminal;

    /**
     * The title of the application.
     */
    private final String title;

    /**
     * The current status of the application.
     */
    private String status = "";

    /**
     * Whether alternative mode is currently enabled.
     */
    private boolean altMode = false;

    /**
     * Creates a new app on the given terminal with the given title. This will
     * clear the current terminal contents.
     * 
     * @param terminal the terminal to act upon
     * @param title the title of the application
     */
    public TerminalApp(Terminal terminal, String title) {
        this.terminal = terminal;
        this.terminal.puts(clear_screen);

        this.title = title;
        this.writeTitle();
    }

    /**
     * Sets the current status of this application.
     * 
     * @param status the status to display
     */
    public void setStatus(String status) {
        this.status = status;
        this.writeTitle();
    }

    /**
     * Returns the current status of this application.
     * 
     * @return the status
     */
    public String getStatus() { return this.status; }

    /**
     * Returns the writer for this application.
     * 
     * @return the writer
     */
    public PrintWriter writer() {
        return this.terminal.writer();
    }

    /**
     * Returns a new {@link LineReaderBuilder} with the underlying terminal
     * preconfigured.
     * 
     * @return the new builder
     */
    public LineReaderBuilder newReader() {
        return LineReaderBuilder.builder().terminal(this.terminal);
    }

    /**
     * Writes an empty line to the terminal.
     */
    public void newLine() {
        this.writer().println();
    }

    /**
     * Clears all contents of the screen, except the title.
     */
    public void clear() {
        this.setCursor(2);
        this.writer().print(ansi().eraseScreen(Erase.FORWARD));
    }

    /**
     * Clears the current line completely, without leaving the current line.
     */
    public void clearLine() {
        this.setCursorX(0);
        this.writer().print(ansi().eraseLine());
    }

    /**
     * Writes the given exception to the terminal.
     * 
     * @param e the exception to write
     */
    public void writeException(Exception e) {
        PrintWriter writer = this.writer();

        this.clearLine();
        writer.println(ansi().newline().bold().fg(Color.RED).a("┌").reset());
        writer.println(ansi().bold()
                .fg(Color.RED)
                .a("│ [")
                .a(e.getClass().getSimpleName())
                .a("] ")
                .a(e.getMessage().replaceAll("\n", "\n│ "))
                .reset());
        writer.println(ansi().bold().fg(Color.RED).a("└").reset().newline());
        this.writer().flush();
    }

    /**
     * Runs the given package on this app.
     * 
     * @param <T> the result type of the package
     * @param pkg the package to run
     * @return the result of the package
     */
    public <T> T run(TerminalPackage<T> pkg) {
        return pkg.execute(this);
    }

    /**
     * Runs the given package in alternate mode (a separate screen buffer), and
     * then returns to the regular screen buffer after execution.
     * 
     * @param <T> the result type of the package
     * @param pkg the package to run
     * @return the result of the package
     */
    public <T> T runAlternate(TerminalPackage<T> pkg) {
        return this.runAlternate(this.status, pkg);
    }

    /**
     * Runs the given package in alternate mode (a separate screen buffer), and
     * then returns to the regular screen buffer after execution. While the
     * package is executing, the given status will be applied.
     * 
     * @param <T> the result type of the package
     * @param status the temporary status to apply
     * @param pkg the package to run
     * @return the result of the package
     */
    public <T> T runAlternate(String status, TerminalPackage<T> pkg) {
        if (this.altMode)
            throw new IllegalStateException("An alternate request was made before the previous one exited.");

        int savedLine = this.getCursor().getY();

        this.terminal.puts(enter_ca_mode);
        this.altMode = true;

        String storedStatus = this.status;
        this.status = status;
        this.writeTitle();

        try {
            return pkg.execute(this);
        } finally {
            this.terminal.puts(exit_ca_mode);
            this.altMode = false;

            this.status = storedStatus;
            this.writeTitle();

            this.setCursor(savedLine);
        }
    }

    /**
     * Returns the current cursor of the terminal.
     * 
     * @return the cursor (0-based)
     */
    public Cursor getCursor() {
        return this.terminal.getCursorPosition(null);
    }

    /**
     * Sets the cursor based on the given X and Y positions.
     * 
     * @param x the new x position of the cursor (0-based)
     * @param y the new y position of the cursor (0-based)
     */
    public void setCursor(int x, int y) {
        this.writer().print(ansi().cursor(y + 1, x + 1));
        this.writer().flush();
    }

    /**
     * Sets the cursor based on the give Y position, moving the cursor to the
     * given line at column 0.
     * 
     * @param y the new y position of the cursor (0-based)
     */
    public void setCursor(int y) {
        this.setCursor(0, y);
    }

    /**
     * Sets the cursor column position on the current line
     * 
     * @param x the new column position of the cursor (0-based)
     */
    public void setCursorX(int x) {
        this.writer().print(ansi().cursorToColumn(x + 1));
        this.writer().flush();
    }

    /**
     * Returns whether there is currently a consumer in alternate mode. If true,
     * any alternate enters will result in an exception.
     * 
     * @return whether alternate mode is occupied
     */
    public boolean isInAlternate() { return this.altMode; }

    /**
     * Writes the current title and status to the application, overwriting the
     * current title.
     */
    private void writeTitle() {
        PrintWriter writer = this.writer();

        this.setCursor(0);
        this.clearLine();
        writer.println(ansi().bold()
                .fgRed()
                .a(this.title)
                .a(this.status.isBlank() ? "" : " - " + this.status)
                .reset());

        this.setCursor(1);
        this.clearLine();
        writer.println(ansi().bold()
                .fgRed()
                .a(this.generateSeperator())
                .reset());

        writer.flush();
    }

    /**
     * Writes a seperator of the given color.
     * 
     * @param color the color of the seperator
     */
    public void writeSeperator(Color color) {
        this.writer()
                .println(ansi().bold()
                        .fg(color)
                        .a(this.generateSeperator())
                        .reset());
    }

    /**
     * Writes a seperator of the default color.
     */
    public void writeSeperator() {
        this.writeSeperator(Color.DEFAULT);
    }

    /**
     * Generates a line that is the size of the current terminal width.
     * 
     * @return a line the width of the terminal
     */
    private String generateSeperator() {
        return "⎯".repeat(this.terminal.getWidth());
    }

}
