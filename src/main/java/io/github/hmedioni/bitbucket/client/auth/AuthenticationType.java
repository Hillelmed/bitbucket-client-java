package io.github.hmedioni.bitbucket.client.auth;

/**
 * Supported Authentication Types for Bitbucket.
 */
public enum AuthenticationType {

    BASIC("Basic"),
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
