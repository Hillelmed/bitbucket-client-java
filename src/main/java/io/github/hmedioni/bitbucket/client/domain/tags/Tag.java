package io.github.hmedioni.bitbucket.client.domain.tags;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Nullable
    private String id;

    @Nullable
    private String displayId;

    @Nullable
    private String type;

    @Nullable
    private String latestCommit;

    @Nullable
    private String latestChangeset;

    @Nullable
    private String hash;

}
