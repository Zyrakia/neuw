package dev.zyrakia.neuw.construction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This wraps a constructor and arguments that can be used to invoke the
 * constructor.
 */
public record Creatable<T>(Constructor<T> constructor, Object[] arguments) {

    /**
     * Uses the underlying arguments to invoke the constructor and create a new
     * instannce.
     * 
     * @return the created instance
     * @see Constructor#newInstance(Object...)
     */
    public T create() throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        return this.constructor.newInstance(this.arguments);
    }
}
