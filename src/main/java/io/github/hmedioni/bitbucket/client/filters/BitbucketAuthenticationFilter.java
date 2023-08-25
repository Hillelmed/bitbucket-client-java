package io.github.hmedioni.bitbucket.client.filters;

import io.github.hmedioni.bitbucket.client.*;
import io.github.hmedioni.bitbucket.client.auth.*;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.*;


public class BitbucketAuthenticationFilter implements ExchangeFilterFunction {
    private final BitbucketAuthentication creds;

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
