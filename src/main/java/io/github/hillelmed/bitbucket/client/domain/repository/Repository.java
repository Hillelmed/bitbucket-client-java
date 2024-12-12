package io.github.hillelmed.bitbucket.client.domain.repository;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hillelmed.bitbucket.client.domain.common.Links;
import io.github.hillelmed.bitbucket.client.domain.common.LinksHolder;
import io.github.hillelmed.bitbucket.client.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * The type Repository.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Repository implements LinksHolder {

    @Nullable
    private String slug;

    private int id;

    @Nullable
    private String name;

    @Nullable
    private String description;

    @Nullable
    private String scmId;

    @Nullable
    private String state;

    @Nullable
    private String statusMessage;

    private boolean forkable;

    @Nullable
    private Repository origin;

    @Nullable
    private Project project;

    @JsonProperty("_public")
    private boolean isPublic;

    private Links links;

    @Nullable
    @Override
    public Links links() {
        return links;
    }
}
