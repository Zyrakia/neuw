package dev.zyrakia.neuw.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * This class is responsible for running a process and allowing easy access to
 * it's output.
 */
public class ProcessBuilderRunner {

    /**
     * The process that this runner launched
     */
    private final Process process;

    /**
     * The thread responsible for reading output.
     */
    private final Thread outputReader;

    /**
     * The process responsible for reading error output.
     */
    private final Thread errorReader;

    /**
     * Starts the process provided by the given process builder, and ensures the
     * outputs of both output and error streams are redirected to the given
     * handlers.
     * 
     * @param pb the process builder to start the process from
     * @param outputHandler the normal line output handler
     * @param errorHandler the error line output handler
     * @throws IOException if there was an error starting the process
     */
    public ProcessBuilderRunner(ProcessBuilder pb,
            Consumer<String> outputHandler, Consumer<String> errorHandler)
            throws IOException {
        this.process = pb.start();

        this.outputReader = ProcessBuilderRunner.consumeStream(this.process
                .getInputStream(), outputHandler == null ? (l) -> {}
                        : outputHandler);
        this.errorReader = ProcessBuilderRunner.consumeStream(this.process
                .getErrorStream(), errorHandler == null ? (l) -> {}
                        : errorHandler);

        this.outputReader.start();
        this.errorReader.start();
    }

    /**
     * Waits for the underlying process to finish executing naturally, and
     * returns the final exit code.
     * 
     * @return the exit code of the process
     * @throws InterruptedException if the process was interrupted before it
     * could finish naturally
     */
    public int waitFor() throws InterruptedException {
        int code = this.process.waitFor();
        this.outputReader.join();
        this.errorReader.join();
        return code;
    }

    /**
     * Terminates the underlying process.
     * 
     * @see Process#destroy()
     */
    public void destroy() {
        this.process.destroy();
    }

    /**
     * Forcibly terminates the underlying process.
     * 
     * @see Process#destroyForcibly()
     */
    public void destroyForcibly() {
        this.process.destroyForcibly();
    }

    /**
     * Returns whether the underlying process is still alive.
     * 
     * @return true if the process is alive, false otherwise
     */
    public boolean isAlive() { return this.process.isAlive(); }

    /**
     * Starts a new thread that uses the given consumer to read the given stream
     * line by line.
     * 
     * @param stream the stream to consume
     * @param consumer the consumer for the stream
     * @return the created thread, not started
     */
    private static Thread consumeStream(InputStream stream,
            Consumer<String> consumer) {
        return new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = reader.readLine()) != null)
                    consumer.accept(line);
            } catch (IOException e) {}
        });
    }

}
