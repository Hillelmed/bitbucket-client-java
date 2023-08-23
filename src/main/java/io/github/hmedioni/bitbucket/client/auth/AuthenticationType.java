package io.github.hmedioni.bitbucket.client.auth;

/**
 * Supported Authentication Types for Bitbucket.
 */
public enum AuthenticationType {

    Basic("Basic"),
    Anonymous("");

    private final String type;

    AuthenticationType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
