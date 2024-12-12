package io.github.hillelmed.bitbucket.client.options;


import io.github.hillelmed.bitbucket.client.domain.participants.Participants;
import io.github.hillelmed.bitbucket.client.domain.pullrequest.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Create participants.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateParticipants {

    private User user;

    @Nullable
    private String lastReviewedCommit;

    private Participants.Role role;

    private boolean approved;

    private Participants.Status status;

}
