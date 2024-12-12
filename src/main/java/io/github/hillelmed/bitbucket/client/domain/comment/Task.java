package io.github.hillelmed.bitbucket.client.domain.comment;


import io.github.hillelmed.bitbucket.client.domain.pullrequest.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Task.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Nullable
    private TaskAnchor anchor;

    @Nullable
    private Author author;

    private long createdDate;

    private int id;

    @Nullable
    private String text;

    @Nullable
    private String state;

    @Nullable
    private PermittedOperations permittedOperations;

}
