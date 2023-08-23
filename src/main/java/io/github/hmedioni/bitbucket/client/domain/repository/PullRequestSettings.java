package io.github.hmedioni.bitbucket.client.domain.repository;


import lombok.*;
import org.springframework.lang.*;


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
