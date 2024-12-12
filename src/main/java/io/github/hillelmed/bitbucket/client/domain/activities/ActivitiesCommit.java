package io.github.hillelmed.bitbucket.client.domain.activities;


import io.github.hillelmed.bitbucket.client.domain.commit.Commit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Activities commit.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivitiesCommit {

    @Nullable
    private List<Commit> commits;

    private long total;
}
