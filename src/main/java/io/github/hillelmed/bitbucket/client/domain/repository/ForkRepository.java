package io.github.hillelmed.bitbucket.client.domain.repository;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Fork repository.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ForkRepository {

    @JsonProperty("name")
    private String newRepositoryName;
    @JsonProperty("project")
    private ProjectKey projectKey;

}
