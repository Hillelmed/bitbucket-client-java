package io.github.hillelmed.bitbucket.client.domain.comment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Permitted operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermittedOperations {

    private boolean editable;

    private boolean deletable;

    private boolean transitionable;

}
