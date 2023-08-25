package io.github.hmedioni.bitbucket.client;


import io.github.hmedioni.bitbucket.client.auth.*;
import org.springframework.lang.*;

import java.util.*;

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

    public static Builder builder() {
        return new Builder();
    }

    @Nullable
    public String authValue() {
        return this.encodedCred;
    }

    public AuthenticationType authType() {
        return authType;
    }

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
