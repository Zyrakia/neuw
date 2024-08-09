package dev.zyrakia.neuw.app.pkg;

import dev.zyrakia.neuw.app.TerminalApp;

/**
 * Represents a sub-application that launches on the given terminal app, and can
 * return a value.
 * 
 * @param <T> the value returned after exceution of this package
 */
public interface TerminalPackage<T> {

    /**
     * Executes this package on the given app.
     */
    public T execute(TerminalApp app);

}
