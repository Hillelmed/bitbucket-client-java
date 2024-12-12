package io.github.hillelmed.bitbucket.client.domain.tags;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Tag.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Nullable
    private String id;

    @Nullable
    private String displayId;

    @Nullable
    private String type;

    @Nullable
    private String latestCommit;

    @Nullable
    private String latestChangeset;

    @Nullable
    private String hash;

}
