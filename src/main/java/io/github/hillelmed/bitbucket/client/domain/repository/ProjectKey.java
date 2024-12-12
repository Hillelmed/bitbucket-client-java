package io.github.hillelmed.bitbucket.client.domain.repository;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Project key.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectKey {

    @JsonProperty("key")
    private String key;


}
