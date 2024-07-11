package dev.zyrakia.neuw.log;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

/**
 * A helper class to output add colour to logged messages.
 */
public class Logger {

    /**
     * Represents the colors used for each log level.
     */
    private static final Map<LogLevel, Color> LVL_COLORS = new EnumMap<>(LogLevel.class);

    /**
     * Represents the prefixes used for each log level.
     */
    private static final Map<LogLevel, String> LVL_PREFIXES = new EnumMap<>(LogLevel.class);

    static {
        LVL_COLORS.put(LogLevel.SUCCESS, GREEN);
        LVL_COLORS.put(LogLevel.INFO, BLUE);
        LVL_COLORS.put(LogLevel.WARN, MAGENTA);
        LVL_COLORS.put(LogLevel.ERR, RED);

        LVL_PREFIXES.put(LogLevel.SUCCESS, "[SUCCESS] > ");
        LVL_PREFIXES.put(LogLevel.INFO, "[INFO] > ");
        LVL_PREFIXES.put(LogLevel.WARN, "[WARNING] > ");
        LVL_PREFIXES.put(LogLevel.ERR, "[ERROR] > ");
    }

    /**
     * Sends a success message to the console.
     * 
     * @param message the message contents
     * @param params  any parameters to be formatted into the message
     */
    public static void success(String message, Object... params) {
        Logger.log(LogLevel.SUCCESS, message, params);
    }

    /**
     * Sends an info message to the console.
     * 
     * @param message the message contents
     * @param params  any parameters to be formatted into the message
     */
    public static void info(String message, Object... params) {
        Logger.log(LogLevel.INFO, message, params);
    }

    /**
     * Sends a warning message to the console.
     * 
     * @param message the message contents
     * @param params  any parameters to be formatted into the message
     */
    public static void warn(String message, Object... params) {
        Logger.log(LogLevel.WARN, message, params);
    }

    /**
     * Sends an error message to the console.
     * 
     * @param message the message contents
     * @param params  any parameters to be formatted into the message
     */
    public static void error(String message, Object... params) {
        Logger.log(LogLevel.ERR, message, params);
    }

    /**
     * Logs the path and adds the given success message.
     * 
     * @param path    the path to log
     * @param message the message content
     * @param params  any parameters to be formatted into the message
     */
    public static void treePathSuccess(Path path, String message, Object... params) {
        Logger.treeLog(LogLevel.SUCCESS, path, message, params);
    }

    /**
     * Logs the path and adds the given info message.
     * 
     * @param path    the path to log
     * @param message the message content
     * @param params  any parameters to be formatted into the message
     */
    public static void treePathInfo(Path path, String message, Object... params) {
        Logger.treeLog(LogLevel.INFO, path, message, params);
    }

    /**
     * Logs the path and adds the given warning message.
     * 
     * @param path    the path to log
     * @param message the message content
     * @param params  any parameters to be formatted into the message
     */
    public static void treePathWarn(Path path, String message, Object... params) {
        Logger.treeLog(LogLevel.WARN, path, message, params);
    }

    /**
     * Logs the path and adds the given error message.
     * 
     * @param path    the path to log
     * @param message the message content
     * @param params  any parameters to be formatted into the message
     */
    public static void treePathError(Path path, String message, Object... params) {
        Logger.treeLog(LogLevel.ERR, path, message, params);
    }

    /**
     * Prints the given message to the console with the prefix of the
     * log level.
     * 
     * @param level   the log level
     * @param message the message content
     * @param params  anyt parameters to be formatted into the message
     */
    public static void log(LogLevel level, String message, Object... params) {
        message = Logger.compileMessage(message, params);

        String prefix = getPrefix(level);
        System.out.println(prefix + message);
    }

    /**
     * Prints the given path and message indented as if it were part of a tree
     * diagram and adds the given message to the end.
     * 
     * @param level   the log level
     * @param path    the path to print
     * @param message the message to add
     * @param params  the parameters to be formatted into the message
     */
    public static void treeLog(LogLevel level, Path path, String message, Object... params) {
        message = Logger.compileMessage(message);

        int indent = path.getNameCount() - 1;
        String name = path.getFileName() == null ? ansi().fg(RED).a("<NONE>").reset().toString()
                : path.getFileName().toString();

        System.out.println(ansi().fg(Logger.getColor(level)).a("    ".repeat(indent)).a("\\").a(name)
                .a(message.isBlank() ? "" : " - ").a(message).reset());
    }

    /**
     * Formats the given message with the given parameters.
     * 
     * @param message the message to format
     * @param params  any parameters to be formatted into the message
     */
    private static String compileMessage(String message, Object... params) {
        return String.format(message, params);
    }

    /**
     * Returns the colour of the given log level, or the default colour.
     * 
     * @param level the log level
     * @return the colour it uses
     */
    private static Color getColor(LogLevel level) {
        if (Logger.LVL_COLORS.containsKey(level)) {
            return Logger.LVL_COLORS.get(level);
        }

        return DEFAULT;
    }

    /**
     * Returns the prefix of the log level in the color of the log level.
     * 
     * @param level the log level
     * @return the prefix coloured properly
     */
    private static String getPrefix(LogLevel level) {
        String prefix = Logger.LVL_PREFIXES.containsKey(level) ? Logger.LVL_PREFIXES.get(level) : "";
        return ansi().fg(Logger.getColor(level)).a(prefix).toString();
    }

}
