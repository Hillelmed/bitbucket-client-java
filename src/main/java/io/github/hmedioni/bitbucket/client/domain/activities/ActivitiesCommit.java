package io.github.hmedioni.bitbucket.client.domain.activities;


import io.github.hmedioni.bitbucket.client.domain.commit.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivitiesCommit {

    @Nullable
    private List<Commit> commits;

    private long total;
}
