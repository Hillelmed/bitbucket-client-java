package io.github.hillelmed.bitbucket.client.features.blocking;


import io.github.hillelmed.bitbucket.client.domain.system.Version;
import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;


/**
 * The interface System api.
 */
@HttpExchange(url = "/rest/api/latest", accept = "application/json", contentType = "application/json")
public interface SystemApi {


    /**
     * Version response entity.
     *
     * @return the response entity
     */
    @GetExchange("/application-properties")
    ResponseEntity<Version> version();
}
