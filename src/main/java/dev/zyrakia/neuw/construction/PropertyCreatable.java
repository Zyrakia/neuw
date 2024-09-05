package dev.zyrakia.neuw.construction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to indicate which constructor or method (factory) can
 * be used to create an object with a set of properties.
 * 
 * Properties must be declared in the order they are declared in the factory,
 * because each properties value is passed as an argument to the factory in
 * order of declaration.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface PropertyCreatable {
    public String[] value();
}
