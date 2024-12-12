package io.github.hillelmed.bitbucket.client.domain.repository;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Pull request settings.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PullRequestSettings {

    @Nullable
    private MergeConfig mergeConfig;

    @Nullable
    private Boolean requiredAllApprovers;

    @Nullable
    private Boolean requiredAllTasksComplete;

    @Nullable
    private Long requiredApprovers;

    @Nullable
    private Long requiredSuccessfulBuilds;

    @Nullable
    private Boolean unapproveOnUpdate;

}
