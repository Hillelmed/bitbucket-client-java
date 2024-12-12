package io.github.hillelmed.bitbucket.client.options;


import io.github.hillelmed.bitbucket.client.domain.comment.MinimalAnchor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The type Create task.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateTask {

    private MinimalAnchor anchor;

    private String text;

    /**
     * Create create task.
     *
     * @param id   the id
     * @param text the text
     * @return the create task
     */
    public static CreateTask create(final int id, final String text) {
        return new CreateTask(new MinimalAnchor(id, "COMMENT"), text);
    }

}
