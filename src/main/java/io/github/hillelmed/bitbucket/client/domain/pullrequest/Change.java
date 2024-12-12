package io.github.hillelmed.bitbucket.client.domain.pullrequest;


import io.github.hillelmed.bitbucket.client.domain.common.Links;
import io.github.hillelmed.bitbucket.client.domain.common.LinksHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Change.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Change implements LinksHolder {

    private String contentId;

    @Nullable
    private String fromContentId;

    private Path path;

    private boolean executable;

    private int percentUnchanged;

    @Nullable
    private String type;

    @Nullable
    private String nodeType;

    @Nullable
    private Path srcPath;

    private boolean srcExecutable;

    @Nullable
    private Links links;

    @Override
    public Links links() {
        return links;
    }


}
