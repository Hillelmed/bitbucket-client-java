package io.github.hmedioni.bitbucket.client.domain.build;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Status {

    private long dateAdded;
    @Nullable
    private String description;
    private String key;
    @Nullable
    private String name;
    private StatusState state;
    private String url;

    public enum StatusState {
        SUCCESSFUL,
        FAILED,
        INPROGRESS
    }

}
