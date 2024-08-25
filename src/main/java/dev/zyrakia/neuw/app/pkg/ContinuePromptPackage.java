package dev.zyrakia.neuw.app.pkg;

import static org.jline.jansi.Ansi.ansi;

import java.io.PrintWriter;
import java.util.List;

import org.jline.jansi.Ansi.Color;
import org.jline.reader.LineReader;

import dev.zyrakia.neuw.app.TerminalApp;

/**
 * This package halts until the user provides input. This package returns null
 * after confirmation.
 */
public class ContinuePromptPackage implements TerminalPackage<Object> {

    /**
     * The default characters that make up the waiting for input animation
     * sequence.
     */
    private static final List<Character> DEF_ANIM_CHARS = List
            .of('⠋', '⠙', '⠚', '⠒', '⠂', '⠂', '⠒', '⠲', '⠴', '⠦', '⠖', '⠒', '⠐', '⠐', '⠒', '⠓', '⠋');

    /**
     * The default time between animation frames for the waiting for input
     * animation sequence.
     */
    private static final int DEF_ANIM_MS = 75;

    /**
     * The characters that make up the waiting for input animation sequence.
     */
    private final List<Character> animChars;

    /**
     * THe time between animation frames for the waiting for input animation
     * sequence.
     */
    private final int animMs;

    /**
     * Creates a new package that will use the given characters and delay for
     * the animation while waiting for confirmation to continue.
     * 
     * @param animChars the character frames of the animation
     * @param animMs the time between frames of the animation
     */
    public ContinuePromptPackage(List<Character> animChars, int animMs) {
        this.animChars = animChars;
        this.animMs = animMs;
    }

    /**
     * Creates a new package that will use the default dots animation.
     */
    public ContinuePromptPackage() {
        this(DEF_ANIM_CHARS, DEF_ANIM_MS);
    }

    /**
     * This is inner line responsible for animating the line that is waiting for
     * user input.
     */
    private class WaitingLine implements Runnable {

        /**
         * The app that will hold the animated line.
         */
        private final TerminalApp app;

        /**
         * The boolean flag indicating whether the line should be animating.
         */
        private volatile boolean active = false;

        /**
         * Creates a new aniamted waiting line that acts upon the given app.
         * 
         * @param app the app that the line will be outputted on
         */
        public WaitingLine(TerminalApp app) {
            this.app = app;
        }

        /**
         * Starts the animation of the line, this will write the line at the
         * current cursor of the terminal.
         * 
         * After this function exits, the animated line will be erased.
         */
        @Override
        public void run() {
            if (this.active) return;
            this.active = true;

            PrintWriter writer = app.writer();
            int targetLine = app.getCursor().getY();

            int i = 0;
            while (this.active) {
                try {
                    Thread.sleep(animMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                app.setCursor(targetLine);
                writer.print(ansi().eraseLine()
                        .bold()
                        .fg(Color.RED)
                        .a("! ")
                        .reset()
                        .a("Press enter to continue ")
                        .a(animChars.get(i++))
                        .a(" "));

                i = i % animChars.size();
            }

            app.setCursor(targetLine);
            writer.print(ansi().eraseLine());
        }

        /**
         * Stops the animation that is currently running, if any.
         */
        public void stopAnimation() {
            this.active = false;
        }

    }

    @Override
    public Void execute(TerminalApp app) {
        WaitingLine line = new WaitingLine(app);
        Thread animThread = new Thread(line);
        animThread.start();

        LineReader reader = app.newReader()
                .option(LineReader.Option.ERASE_LINE_ON_FINISH, true)
                .build();
        reader.readLine();
        line.stopAnimation();

        try {
            animThread.join();
        } catch (InterruptedException e) {}

        return null;
    }

}
