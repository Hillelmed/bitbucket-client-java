package io.github.hmedioni.bitbucket.client.config;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.auth.*;
import lombok.*;

@Data
@AllArgsConstructor
public class BitbucketProperties {

    private String url;
    private String user;
    private String password;

    public BitbucketProperties(String url) {
        this.url = url;
        this.user = null;
        this.password = null;
    }

    public BitbucketProperties() {
        this.url = BitbucketUtils.inferEndpoint();
    }

    public BitbucketAuthentication bitbucketAuthentication() {
        if (this.user == null) {
            return BitbucketUtils.inferAuthentication();
        } else {
            return new BitbucketAuthentication(this.user + ":" + this.password,
                    AuthenticationType.Basic);
        }
    }
}
