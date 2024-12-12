package io.github.hillelmed.bitbucket.client.domain.activities;


import io.github.hillelmed.bitbucket.client.domain.comment.Comments;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Activities.
 */
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

    /**
     * The enum Activities type.
     */
    public enum ActivitiesType {
        /**
         * Declined activities type.
         */
        DECLINED,
        /**
         * Rescoped activities type.
         */
        RESCOPED,
        /**
         * Approved activities type.
         */
        APPROVED,
        /**
         * Reviewed activities type.
         */
        REVIEWED,
        /**
         * Commented activities type.
         */
        COMMENTED,
        /**
         * Opened activities type.
         */
        OPENED,
        /**
         * Updated activities type.
         */
        UPDATED,
        /**
         * Unapproved activities type.
         */
        UNAPPROVED,
        /**
         * Reopened activities type.
         */
        REOPENED,
        /**
         * Merged activities type.
         */
        MERGED
    }

}
