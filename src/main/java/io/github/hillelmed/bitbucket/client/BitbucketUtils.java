package io.github.hillelmed.bitbucket.client;

import org.springframework.lang.Nullable;

import static io.github.hillelmed.bitbucket.client.BitbucketConstants.*;


/**
 * Collection of static methods to be used globally.
 */
public class BitbucketUtils {


    /**
     * Instantiates a new Bitbucket utils.
     */
    protected BitbucketUtils() {
        throw new UnsupportedOperationException("Purposefully not implemented");
    }

    /**
     * If the passed systemProperty is non-null we will attempt to query
     * the `System Properties` for a value and return it. If no value
     * was found, and environmentVariable is non-null, we will attempt to
     * query the `Environment Variables` for a value and return it. If
     * both are either null or can't be found than null will be returned.
     *
     * @param systemProperty      possibly existent System Property.
     * @param environmentVariable possibly existent Environment Variable.
     * @return found external value or null.
     */
    public static String retrieveExternalValue(@Nullable final String systemProperty,
                                               @Nullable final String environmentVariable) {

        // 1.) Search for System Property
        if (systemProperty != null) {
            final String value = System.getProperty(systemProperty);
            if (value != null) {
                return value;
            }
        }

        if (environmentVariable != null) {
            return System.getenv(environmentVariable);
        }

        return null;
    }

    /**
     * Find endpoint searching first within `System Properties` and
     * then within `Environment Variables` returning whichever has a
     * value first.
     *
     * @return endpoint or null if it can't be found.
     */
    public static String inferEndpoint() {
        final String possibleValue = BitbucketUtils
                .retrieveExternalValue(ENDPOINT_SYSTEM_PROPERTY,
                        ENDPOINT_ENVIRONMENT_VARIABLE);
        return possibleValue != null ? possibleValue : DEFAULT_ENDPOINT;
    }

    /**
     * Find credentials (Basic, Bearer, or Anonymous) from system/environment.
     *
     * @return BitbucketCredentials bitbucket authentication
     */
    public static BitbucketAuthentication inferAuthentication() {

        // 1.) Check for "Basic" auth credentials.
        final BitbucketAuthentication.Builder inferAuth = BitbucketAuthentication.builder();
        String authValue = BitbucketUtils
                .retrieveExternalValue(CREDENTIALS_SYSTEM_PROPERTY,
                        CREDENTIALS_ENVIRONMENT_VARIABLE);
        if (authValue != null) {
            inferAuth.credentials(authValue);
        }

        return inferAuth.build();
    }

}
