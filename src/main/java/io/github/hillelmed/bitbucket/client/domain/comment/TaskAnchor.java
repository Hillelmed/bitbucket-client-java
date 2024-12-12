package io.github.hillelmed.bitbucket.client.domain.comment;


import io.github.hillelmed.bitbucket.client.domain.pullrequest.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * The type Task anchor.
 */
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
