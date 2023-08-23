package io.github.hmedioni.bitbucket.client.domain.repository;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HookDetails {

    private String key;

    private String name;

    private String type;

    private String description;

    private String version;

    @Nullable
    private String configFormKey;

}
