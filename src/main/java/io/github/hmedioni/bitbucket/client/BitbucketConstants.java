package io.github.hmedioni.bitbucket.client;

/**
 * Various constants that can be used in a global context.
 */
public class BitbucketConstants {

    public static final String ENDPOINT_SYSTEM_PROPERTY = "bitbucket.rest.endpoint";
    public static final String ENDPOINT_ENVIRONMENT_VARIABLE = ENDPOINT_SYSTEM_PROPERTY.replace("\\.", "_").toUpperCase();

    public static final String CREDENTIALS_SYSTEM_PROPERTY = "bitbucket.rest.credentials";
    public static final String CREDENTIALS_ENVIRONMENT_VARIABLE = CREDENTIALS_SYSTEM_PROPERTY.replace("\\.", "_").toUpperCase();

    public static final String TOKEN_SYSTEM_PROPERTY = "bitbucket.rest.token";
    public static final String TOKEN_ENVIRONMENT_VARIABLE = TOKEN_SYSTEM_PROPERTY.replace("\\.", "_").toUpperCase();

    public static final String DEFAULT_ENDPOINT = "http://127.0.0.1:7990";
    public static final String DEFAULT_VERSION = "latest";

    public static final String JCLOUDS_PROPERTY_ID = "jclouds.";
    public static final String BITBUCKET_REST_PROPERTY_ID = "bitbucket.rest." + JCLOUDS_PROPERTY_ID;

    public static final String JCLOUDS_VARIABLE_ID = "JCLOUDS_";
    public static final String BITBUCKET_REST_VARIABLE_ID = "BITBUCKET_REST_" + JCLOUDS_VARIABLE_ID;

    protected BitbucketConstants() {
        throw new UnsupportedOperationException("Purposefully not implemented");
    }
}
