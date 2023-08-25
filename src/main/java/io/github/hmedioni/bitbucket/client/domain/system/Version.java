package io.github.hmedioni.bitbucket.client.domain.system;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Version {

    @JsonProperty("version")
    private String id;

    private String buildNumber;

    private String buildDate;

    private String displayName;


}
