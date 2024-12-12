package io.github.hillelmed.bitbucket.client.filters;

import io.github.hillelmed.bitbucket.client.BitbucketAuthentication;
import io.github.hillelmed.bitbucket.client.auth.AuthenticationType;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;


/**
 * The type Bitbucket authentication filter.
 */
public class BitbucketAuthenticationFilter implements ExchangeFilterFunction {
    private final BitbucketAuthentication creds;

    /**
     * Instantiates a new Bitbucket authentication filter.
     *
     * @param creds the creds
     */
    public BitbucketAuthenticationFilter(final BitbucketAuthentication creds) {
        this.creds = creds;
    }


    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        if (creds.authType() == AuthenticationType.ANONYMOUS) {
            return next.exchange(request);
        } else {
            final String authHeader = creds.authType() + " " + creds.authValue();

            ClientRequest newRequest = ClientRequest.from(request)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .build();
            return next.exchange(newRequest);
        }
    }
}
