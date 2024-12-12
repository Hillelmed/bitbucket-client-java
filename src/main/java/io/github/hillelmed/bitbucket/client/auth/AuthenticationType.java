package io.github.hillelmed.bitbucket.client.auth;

/**
 * Supported Authentication Types for Bitbucket.
 */
public enum AuthenticationType {

    /**
     * Basic authentication type.
     */
    BASIC("Basic"),
    /**
     * Anonymous authentication type.
     */
    ANONYMOUS("");

    private final String type;

    AuthenticationType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
