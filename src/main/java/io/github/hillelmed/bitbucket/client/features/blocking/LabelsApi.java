package io.github.hillelmed.bitbucket.client.features.blocking;

import io.github.hillelmed.bitbucket.client.domain.labels.Label;
import io.github.hillelmed.bitbucket.client.domain.labels.LabelsPage;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;


/**
 * The interface Labels api.
 */
@HttpExchange(url = "/rest/api/latest", accept = "application/json", contentType = "application/json")
public interface LabelsApi {


    /**
     * List response entity.
     *
     * @param prefix the prefix
     * @return the response entity
     */
    @GetExchange("/labels")
    ResponseEntity<LabelsPage> list(@Nullable @RequestParam(required = false, name = "prefix") String prefix);


    /**
     * Gets label by name.
     *
     * @param labelName the label name
     * @return the label by name
     */
    @GetExchange("/labels/{labelName}")
    ResponseEntity<Label> getLabelByName(@PathVariable("labelName") String labelName);
}
