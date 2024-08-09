package dev.zyrakia.neuw.app;

import static org.jline.jansi.Ansi.ansi;

import java.io.PrintWriter;

import org.jline.jansi.Ansi.Color;
import org.jline.jansi.Ansi.Erase;
import org.jline.reader.LineReaderBuilder;
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
     * The stored title for when the application enters alternate mode with a
     * status.
     */
    private String storedStatus = "";

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
     * Enters the alternate screen buffer.
     */
    public void enterAlternate() {
        this.enterAlternate(this.status);
    }

    /**
     * Enters the alternate screen buffer with the given status. The next exit
     * will revert the status to the status that was applied before the
     * alternate screen buffer was entered.
     * 
     * @param status the status to enter with
     */
    public void enterAlternate(String status) {
        if (this.altMode) return;

        this.terminal.puts(enter_ca_mode);
        this.altMode = true;

        this.storedStatus = this.status;
        this.status = status;
        this.writeTitle();
    }

    /**
     * Exits the alternate screen buffer.
     */
    public void exitAlternate() {
        if (!this.altMode) return;

        this.terminal.puts(exit_ca_mode);
        this.altMode = false;

        this.status = storedStatus;
        this.writeTitle();
    }

    /**
     * Clears all contents of the screen, except the title.
     */
    public void clear() {
        this.writer().print(ansi().cursor(3, 1).eraseScreen(Erase.FORWARD));
    }

    /**
     * Clears the current line completely, without leaving the current line.
     */
    public void clearLine() {
        this.writer().print(ansi().cursorToColumn(0).eraseLine().reset());
    }

    /**
     * Moves to the last line, while clearing it, and stays on the line.
     */
    public void clearLastLine() {
        this.writer().print(ansi().cursorUpLine().eraseLine().reset());
    }

    /**
     * Writes the given exception to the terminal.
     * 
     * @param e the exception to write
     */
    public void writeException(Exception e) {
        PrintWriter writer = this.writer();

        writer.println(ansi().cursorToColumn(1).eraseLine());
        writer.println(ansi().bold().fg(Color.RED).a("┌").reset());
        writer.println(ansi().bold()
                .fg(Color.RED)
                .a("│ [")
                .a(e.getClass().getSimpleName())
                .a("] ")
                .a(e.getMessage())
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
     * Writes the current title and status to the application, overwriting the
     * current title.
     */
    private void writeTitle() {
        PrintWriter writer = this.writer();

        writer.print(ansi().cursor(1, 1)
                .eraseLine()
                .bold()
                .fgRed()
                .a(this.title)
                .a(this.status.isBlank() ? "" : " - " + this.status)
                .reset());

        writer.println(ansi().cursor(2, 1)
                .eraseLine()
                .bold()
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
