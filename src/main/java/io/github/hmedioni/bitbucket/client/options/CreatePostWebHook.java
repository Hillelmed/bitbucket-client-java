package io.github.hmedioni.bitbucket.client.options;


import lombok.*;
import org.springframework.lang.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatePostWebHook {
    public boolean branchCreated;

    public boolean branchDeleted;

    @Nullable
    public String branchesToIgnore;

    @Nullable
    public String committersToIgnore;

    public boolean enabled;

    public boolean prCommented;

    public boolean prCreated;

    public boolean prDeclined;

    public boolean prMerged;

    public boolean prReopened;

    public boolean prRescoped;

    public boolean prUpdated;

    public boolean repoMirrorSynced;

    public boolean repoPush;

    public boolean tagCreated;

    public String title;

    public String url;

}
