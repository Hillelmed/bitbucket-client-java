package io.github.hillelmed.bitbucket.client;

/**
 * Various constants that can be used in a global context.
 */
public class BitbucketConstants {

    /**
     * The constant REFS_HEADS_MASTER.
     */
    public static final String REFS_HEADS_MASTER = "refs/heads/master";
    /**
     * The constant ENDPOINT_SYSTEM_PROPERTY.
     */
    public static final String ENDPOINT_SYSTEM_PROPERTY = "bitbucket.rest.endpoint";
    /**
     * The constant ENDPOINT_ENVIRONMENT_VARIABLE.
     */
    public static final String ENDPOINT_ENVIRONMENT_VARIABLE = ENDPOINT_SYSTEM_PROPERTY.replace("\\.", "_").toUpperCase();

    /**
     * The constant CREDENTIALS_SYSTEM_PROPERTY.
     */
    public static final String CREDENTIALS_SYSTEM_PROPERTY = "bitbucket.rest.credentials";
    /**
     * The constant CREDENTIALS_ENVIRONMENT_VARIABLE.
     */
    public static final String CREDENTIALS_ENVIRONMENT_VARIABLE = CREDENTIALS_SYSTEM_PROPERTY.replace("\\.", "_").toUpperCase();

    /**
     * The constant DEFAULT_ENDPOINT.
     */
    public static final String DEFAULT_ENDPOINT = "http://127.0.0.1:7990";

    /**
     * Instantiates a new Bitbucket constants.
     */
    protected BitbucketConstants() {
        throw new UnsupportedOperationException("Purposefully not implemented");
    }
}
