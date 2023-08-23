package io.github.hmedioni.bitbucket.client.features.blocking;

import io.github.hmedioni.bitbucket.client.domain.labels.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;


@HttpExchange(url = "/rest/api/latest", accept = "application/json", contentType = "application/json")
public interface LabelsApi {


    @GetExchange("/labels")
    ResponseEntity<LabelsPage> list(@Nullable @RequestParam(required = false, name = "prefix") String prefix);


    @GetExchange("/labels/{labelName}")
    ResponseEntity<Label> getLabelByName(@PathVariable("labelName") String labelName);
}
