package io.github.hillelmed.bitbucket.client.domain.file;


import io.github.hillelmed.bitbucket.client.domain.commit.Commit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Map;


/**
 * The type Last modified.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastModified {

    @Nullable
    private Map<String, Commit> files;

    @Nullable
    private Commit latestCommit;

}
