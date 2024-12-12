package io.github.hillelmed.bitbucket.client.domain.system;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Version.
 */
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
