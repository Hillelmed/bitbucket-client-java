package io.github.hillelmed.bitbucket.client;


import io.github.hillelmed.bitbucket.client.auth.AuthenticationType;
import org.springframework.lang.Nullable;

import java.util.Base64;
import java.util.Objects;

/**
 * Credentials instance for Bitbucket authentication.
 */
public class BitbucketAuthentication {

    private final AuthenticationType authType;
    private final String encodedCred;

    /**
     * Create instance of BitbucketAuthentication
     *
     * @param authValue value to use for authentication type HTTP header.
     * @param authType  authentication type (e.g. Basic, Bearer, Anonymous).
     */
    public BitbucketAuthentication(final String authValue, final AuthenticationType authType) {
        if (authType == AuthenticationType.BASIC) {
            this.encodedCred = Base64.getEncoder().encodeToString(authValue.getBytes());
        } else {
            this.encodedCred = "";
        }
        this.authType = authType;
    }

    /**
     * Builder builder.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Auth value string.
     *
     * @return the string
     */
    @Nullable
    public String authValue() {
        return this.encodedCred;
    }

    /**
     * Auth type authentication type.
     *
     * @return the authentication type
     */
    public AuthenticationType authType() {
        return authType;
    }

    /**
     * The type Builder.
     */
    public static class Builder {

        private String authValue;
        private AuthenticationType authType;

        /**
         * Set 'Basic' credentials.
         *
         * @param basicCredentials value to use for 'Basic' credentials.
         * @return this Builder.
         */
        public Builder credentials(final String basicCredentials) {
            this.authValue = Objects.requireNonNull(basicCredentials);
            this.authType = AuthenticationType.BASIC;
            return this;
        }


        /**
         * Build and instance of BitbucketCredentials.
         *
         * @return instance of BitbucketCredentials.
         */
        public BitbucketAuthentication build() {
            return new BitbucketAuthentication(authValue, authType != null
                    ? authType
                    : AuthenticationType.ANONYMOUS);
        }
    }
}
