package io.github.hmedioni.bitbucket.client.domain.commit;


import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commit {

    private String id;

    private String displayId;

    @Nullable
    private Author author;

    private long authorTimestamp;

    @Nullable
    private Author committer;

    private long committerTimestamp;

    @Nullable
    private String message;

    @Nullable
    private Map<String, Object> properties;

    private List<Parents> parents;

}
