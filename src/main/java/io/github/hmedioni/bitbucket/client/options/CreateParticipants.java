package io.github.hmedioni.bitbucket.client.options;


import io.github.hmedioni.bitbucket.client.domain.participants.*;
import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;


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
