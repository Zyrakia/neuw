package dev.zyrakia.neuw.log;

/**
 * Represents a logging message level.
 */
public enum LogLevel {
    /**
     * Indicates a successful operation.
     */
    SUCCESS,

    /**
     * Indicates a neutral operation or status message.
     */
    INFO,

    /**
     * Indicates a potential problem.
     */
    WARN,

    /**
     * Indicates an error, an existing problem.
     */
    ERR;
}
