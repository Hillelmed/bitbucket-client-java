package io.github.hmedioni.bitbucket.client.domain.file;


import io.github.hmedioni.bitbucket.client.domain.commit.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastModified {

    @Nullable
    private Map<String, Commit> files;

    @Nullable
    private Commit latestCommit;

}
