package io.github.hillelmed.bitbucket.client;

import io.github.hillelmed.bitbucket.client.auth.AuthenticationType;
import io.github.hillelmed.bitbucket.client.config.BitbucketProperties;
import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.Closeable;
import java.io.IOException;

/**
 * The type Bitbucket client.
 */
public final class BitbucketClient implements Closeable {

    private final BitbucketApi bitbucketApi;
    @Getter
    private final BitbucketAuthentication bitbucketAuthentication;

    /**
     * Create an BitbucketClient. If any of the passed in variables are null we
     * will query System Properties and Environment Variables, in order, to
     * search for values that may be set in a devops/CI fashion. The only
     * difference is the `overrides` which gets merged, but takes precedence,
     * with those System Properties and Environment Variables found.
     * <p>
     * //     * @param endPoint       URL of Bitbucket instance.
     * //     * @param authentication authentication used to connect to Bitbucket instance.
     * //     * @param overrides      jclouds Properties to override defaults when creating a new BitbucketApi.
     * //     * @param modules        a list of modules to be passed to the Contextbuilder, e.g. for logging.
     */
    private BitbucketClient(@Nullable BitbucketProperties bitbucketProperties, BitbucketApi bitbucketApi) {
        BitbucketAuthentication bitbucketAuthenticationTmp;
        if (bitbucketProperties != null) {
            if (bitbucketProperties.getUrl() == null) {
                bitbucketProperties.setUrl(BitbucketUtils.inferEndpoint());
            }
            if (bitbucketProperties.getUser() == null) {
                bitbucketAuthenticationTmp = BitbucketUtils.inferAuthentication();
            } else {
                bitbucketAuthenticationTmp = new BitbucketAuthentication(bitbucketProperties.getUser() + ":" + bitbucketProperties.getPassword(),
                        AuthenticationType.BASIC);
            }
        } else {
            bitbucketProperties = new BitbucketProperties();
            bitbucketProperties.setUrl(BitbucketUtils.inferEndpoint());
            bitbucketAuthenticationTmp = BitbucketUtils.inferAuthentication();
        }
        this.bitbucketAuthentication = bitbucketAuthenticationTmp;
        this.bitbucketApi = bitbucketApi;
    }

    /**
     * Create a BitbucketClient inferring endpoint and authentication from
     * environment and system properties.
     *
     * @param bitbucketProperties the bitbucket properties
     * @return the bitbucket client
     */
    public static BitbucketClient create(BitbucketProperties bitbucketProperties) {
        return new BitbucketClient(bitbucketProperties, bitbucketApi(bitbucketProperties, null));
    }

    /**
     * Create bitbucket client.
     *
     * @param bitbucketProperties the bitbucket properties
     * @param webClient           the web client
     * @return the bitbucket client
     */
    public static BitbucketClient create(BitbucketProperties bitbucketProperties, WebClient webClient) {
        return new BitbucketClient(bitbucketProperties, bitbucketApi(bitbucketProperties, webClient));
    }

    private static BitbucketApi bitbucketApi(BitbucketProperties bitbucketProperties, WebClient webClient) {
        return new BitbucketApiClientImpl(bitbucketProperties, webClient);
    }

    /**
     * Auth value string.
     *
     * @return the string
     */
    public String authValue() {
        return this.bitbucketAuthentication.authValue();
    }


    /**
     * Api bitbucket api.
     *
     * @return the bitbucket api
     */
    public BitbucketApi api() {
        return this.bitbucketApi;
    }

    @Override
    public void close() throws IOException {
        if (this.api() != null) {
            this.api().close();
        }
    }

}
