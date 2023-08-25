package io.github.hmedioni.bitbucket.client.options;


import lombok.*;
import org.springframework.lang.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatePostWebHook {
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

    private String title;

    private String url;

}
