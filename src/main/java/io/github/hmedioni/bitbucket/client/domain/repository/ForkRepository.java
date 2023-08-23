package io.github.hmedioni.bitbucket.client.domain.repository;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ForkRepository {

    @JsonProperty("name")
    private String newRepositoryName;
    @JsonProperty("project")
    private ProjectKey projectKey;

}
