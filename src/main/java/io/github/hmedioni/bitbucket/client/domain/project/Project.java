package io.github.hmedioni.bitbucket.client.domain.project;


import com.fasterxml.jackson.annotation.*;
import io.github.hmedioni.bitbucket.client.domain.common.*;
import lombok.*;
import org.springframework.lang.*;


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
