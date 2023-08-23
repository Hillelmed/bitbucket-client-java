package io.github.hmedioni.bitbucket.client.domain.activities;


import io.github.hmedioni.bitbucket.client.domain.commit.*;
import org.springframework.lang.*;

import java.util.*;


public class ActivitiesCommit {

    @Nullable
    public List<Commit> commits;

    public long total;
}
