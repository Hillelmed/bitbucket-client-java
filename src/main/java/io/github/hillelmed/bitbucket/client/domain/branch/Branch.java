package io.github.hillelmed.bitbucket.client.domain.branch;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * The type Branch.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

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

    private boolean isDefault;

    // This map consists of data provided by plugins and so
    // is non-standard in how it's returned and the fields
    // it has. As such we return the raw JsonNode and instead
    // let the caller iterate through the returned plugin data
    // for what they are looking for.
    private Map<String, Object> metadata;


}
