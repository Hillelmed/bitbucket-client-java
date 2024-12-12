package io.github.hillelmed.bitbucket.client.domain.project;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hillelmed.bitbucket.client.domain.common.Links;
import io.github.hillelmed.bitbucket.client.domain.common.LinksHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Project.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Project implements LinksHolder {

    @Nullable
    private String key;

    private int id;

    @Nullable
    private String name;

    @Nullable
    private String description;

    @JsonProperty("_public")
    private boolean isPublic;

    @Nullable
    private String type;


    @Nullable
    private Links links;


    @Override
    public Links links() {
        return links;
    }
}
