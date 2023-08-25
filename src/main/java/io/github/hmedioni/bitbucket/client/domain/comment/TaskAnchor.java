package io.github.hmedioni.bitbucket.client.domain.comment;


import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAnchor {

    private Map<String, Object> properties;

    private int id;

    private int version;

    @Nullable
    private String text;

    private Author author;

    private long createdDate;

    private long updatedDate;

    @Nullable
    private PermittedOperations permittedOperations;

    @Nullable
    private String type;

}
