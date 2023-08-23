package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import io.github.hmedioni.bitbucket.client.domain.common.*;
import lombok.*;
import org.springframework.lang.*;


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
