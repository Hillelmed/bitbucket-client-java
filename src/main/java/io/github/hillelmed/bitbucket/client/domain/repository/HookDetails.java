package io.github.hillelmed.bitbucket.client.domain.repository;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Hook details.
 */
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
