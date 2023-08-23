package io.github.hmedioni.bitbucket.client.domain.branch;


import lombok.*;
import org.springframework.lang.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Branch {

    @Nullable
    public String id;

    @Nullable
    public String displayId;

    @Nullable
    public String type;

    @Nullable
    public String latestCommit;

    @Nullable
    public String latestChangeset;

    public boolean isDefault;

    // This map consists of data provided by plugins and so
    // is non-standard in how it's returned and the fields
    // it has. As such we return the raw JsonNode and instead
    // let the caller iterate through the returned plugin data
    // for what they are looking for.
    public Map<String, Object> metadata;


}
