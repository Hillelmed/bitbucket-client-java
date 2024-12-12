package io.github.hillelmed.bitbucket.client.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides a potential list of URI's for further documentation.
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Documentation {

    /**
     * List of documentation URI's.
     *
     * @return list of URI's.
     */
    String[] value();
}
