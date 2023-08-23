package io.github.hmedioni.bitbucket.client.features.blocking;


import io.github.hmedioni.bitbucket.client.domain.system.*;
import org.springframework.http.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/api/latest", accept = "application/json", contentType = "application/json")
public interface SystemApi {


    @GetExchange("/application-properties")
    ResponseEntity<Version> version();
}
