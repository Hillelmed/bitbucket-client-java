package io.github.hmedioni.bitbucket.client;

/**
 * Various constants that can be used in a global context.
 */
public class BitbucketConstants {

    public static final String REFS_HEADS_MASTER = "refs/heads/master";
    public static final String ENDPOINT_SYSTEM_PROPERTY = "bitbucket.rest.endpoint";
    public static final String ENDPOINT_ENVIRONMENT_VARIABLE = ENDPOINT_SYSTEM_PROPERTY.replace("\\.", "_").toUpperCase();

    public static final String CREDENTIALS_SYSTEM_PROPERTY = "bitbucket.rest.credentials";
    public static final String CREDENTIALS_ENVIRONMENT_VARIABLE = CREDENTIALS_SYSTEM_PROPERTY.replace("\\.", "_").toUpperCase();

    public static final String DEFAULT_ENDPOINT = "http://127.0.0.1:7990";

    protected BitbucketConstants() {
        throw new UnsupportedOperationException("Purposefully not implemented");
    }
}
