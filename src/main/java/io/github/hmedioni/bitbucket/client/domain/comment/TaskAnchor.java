package io.github.hmedioni.bitbucket.client.domain.comment;


import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAnchor {

    public Map<String, Object> properties;

    public int id;

    public int version;

    @Nullable
    public String text;

    public Author author;

    public long createdDate;

    public long updatedDate;

    @Nullable
    public PermittedOperations permittedOperations;

    @Nullable
    public String type;

}
