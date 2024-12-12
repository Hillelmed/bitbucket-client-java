package io.github.hillelmed.bitbucket.client.domain.participants;


import io.github.hillelmed.bitbucket.client.domain.pullrequest.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Participants.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Participants {


    @Nullable
    private User user;
    @Nullable
    private String lastReviewedCommit;
    @Nullable
    private Role role;
    private boolean approved;
    @Nullable
    private Status status;

    /**
     * The enum Role.
     */
    public enum Role {
        /**
         * Author role.
         */
        AUTHOR,
        /**
         * Reviewer role.
         */
        REVIEWER,
        /**
         * Participant role.
         */
        PARTICIPANT
    }

    /**
     * The enum Status.
     */
    public enum Status {
        /**
         * Approved status.
         */
        APPROVED,
        /**
         * Unapproved status.
         */
        UNAPPROVED,
        /**
         * Needs work status.
         */
        NEEDS_WORK
    }

}
