package io.github.hillelmed.bitbucket.client.domain.search;


import org.springframework.lang.Nullable;


/**
 * The type Search hit context.
 */
public class SearchHitContext {

    /**
     * The Line.
     */
    @Nullable
    public String line;

    /**
     * The Text.
     */
    @Nullable
    public String text;
}
