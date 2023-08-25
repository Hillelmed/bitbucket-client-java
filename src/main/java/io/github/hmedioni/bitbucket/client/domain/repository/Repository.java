package io.github.hmedioni.bitbucket.client.domain.repository;


import com.fasterxml.jackson.annotation.*;
import io.github.hmedioni.bitbucket.client.domain.common.*;
import io.github.hmedioni.bitbucket.client.domain.project.*;
import lombok.*;
import org.springframework.lang.*;

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
