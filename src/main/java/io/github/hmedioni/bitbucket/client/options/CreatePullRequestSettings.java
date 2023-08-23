package io.github.hmedioni.bitbucket.client.options;


import io.github.hmedioni.bitbucket.client.domain.repository.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePullRequestSettings {

    private MergeConfig mergeConfig;

    private boolean requiredAllApprovers;

    private boolean requiredAllTasksComplete;

    private long requiredApprovers;

    private long requiredSuccessfulBuilds;

    private boolean unapproveOnUpdate;

}
