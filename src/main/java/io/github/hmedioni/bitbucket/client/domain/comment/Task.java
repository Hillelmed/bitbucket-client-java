package io.github.hmedioni.bitbucket.client.domain.comment;


import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;
import org.springframework.lang.*;


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
