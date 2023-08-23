package io.github.hmedioni.bitbucket.client.domain.activities;


import io.github.hmedioni.bitbucket.client.domain.comment.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activities {

    private long id;
    private long createdDate;
    private User user;
    private ActivitiesType action;
    @Nullable
    private String commentAction;
    @Nullable
    private Comments comment;
    @Nullable
    private String fromHash;
    @Nullable
    private String previousFromHash;
    @Nullable
    private String previousToHash;
    @Nullable
    private String toHash;
    @Nullable
    private ActivitiesCommit added;
    @Nullable
    private ActivitiesCommit removed;
    @Nullable
    private List<User> addedReviewers;
    @Nullable
    private List<User> removedReviewers;

    public enum ActivitiesType {
        DECLINED,
        RESCOPED,
        APPROVED,
        REVIEWED,
        COMMENTED,
        OPENED,
        UPDATED,
        UNAPPROVED,
        REOPENED,
        MERGED
    }

}
