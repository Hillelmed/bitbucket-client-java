package io.github.hmedioni.bitbucket.client.annotations;

import java.lang.annotation.*;

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
