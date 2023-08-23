package io.github.hmedioni.bitbucket.client.domain.participants;


import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;


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

    public enum Role {
        AUTHOR,
        REVIEWER,
        PARTICIPANT
    }

    public enum Status {
        APPROVED,
        UNAPPROVED,
        NEEDS_WORK
    }

}
