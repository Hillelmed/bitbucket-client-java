package io.github.hillelmed.bitbucket.client.domain.commit;


import io.github.hillelmed.bitbucket.client.domain.pullrequest.Author;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.Parents;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;


/**
 * The type Commit.
 */
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
