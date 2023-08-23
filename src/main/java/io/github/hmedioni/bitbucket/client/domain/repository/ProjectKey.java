package io.github.hmedioni.bitbucket.client.domain.repository;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectKey {

    @JsonProperty("key")
    private String projectKey;


}
