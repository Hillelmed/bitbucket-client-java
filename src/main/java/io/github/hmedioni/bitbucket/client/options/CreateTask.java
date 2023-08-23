package io.github.hmedioni.bitbucket.client.options;


import io.github.hmedioni.bitbucket.client.domain.comment.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateTask {

    private MinimalAnchor anchor;

    private String text;

    public static CreateTask create(final int id, final String text) {
        return new CreateTask(new MinimalAnchor(id, "COMMENT"), text);
    }

}
