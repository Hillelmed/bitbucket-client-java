package io.github.hmedioni.bitbucket.client.domain.system;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Version {

    private String version;

    private String buildNumber;

    private String buildDate;

    private String displayName;


}
