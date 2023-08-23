package io.github.hmedioni.bitbucket.client.domain.postwebhooks;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostWebHook {

    private boolean branchCreated;

    private boolean branchDeleted;

    @Nullable
    private String branchesToIgnore;

    @Nullable
    private String committersToIgnore;

    private boolean enabled;

    private boolean prCommented;

    private boolean prCreated;

    private boolean prDeclined;

    private boolean prMerged;

    private boolean prReopened;

    private boolean prRescoped;

    private boolean prUpdated;

    private boolean repoMirrorSynced;

    private boolean repoPush;

    private boolean tagCreated;

    @Nullable
    private String title;

    @Nullable
    private String url;

}
