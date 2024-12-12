package io.github.hillelmed.bitbucket.client.config;

import io.github.hillelmed.bitbucket.client.BitbucketAuthentication;
import io.github.hillelmed.bitbucket.client.BitbucketUtils;
import io.github.hillelmed.bitbucket.client.auth.AuthenticationType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Bitbucket properties.
 */
@Data
@AllArgsConstructor
public class BitbucketProperties {

    private String url;
    private String user;
    private String password;

    /**
     * Instantiates a new Bitbucket properties.
     *
     * @param url the url
     */
    public BitbucketProperties(String url) {
        this.url = url;
        this.user = null;
        this.password = null;
    }

    /**
     * Instantiates a new Bitbucket properties.
     */
    public BitbucketProperties() {
        this.url = BitbucketUtils.inferEndpoint();
    }

    /**
     * Bitbucket authentication bitbucket authentication.
     *
     * @return the bitbucket authentication
     */
    public BitbucketAuthentication bitbucketAuthentication() {
        if (this.user == null) {
            return BitbucketUtils.inferAuthentication();
        } else {
            return new BitbucketAuthentication(this.user + ":" + this.password,
                    AuthenticationType.BASIC);
        }
    }
}
