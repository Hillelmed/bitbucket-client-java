package io.github.hillelmed.bitbucket.client.options;


import io.github.hillelmed.bitbucket.client.domain.repository.MergeConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Create pull request settings.
 */
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
