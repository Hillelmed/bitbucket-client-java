package io.github.hmedioni.bitbucket.client.options;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBuildStatus {

    private STATE state;
    private String key;
    @Nullable
    private String name;
    private String url;
    @Nullable
    private String description;

    public enum STATE {
        SUCCESSFUL,
        FAILED,
        INPROGRESS
    }

}
