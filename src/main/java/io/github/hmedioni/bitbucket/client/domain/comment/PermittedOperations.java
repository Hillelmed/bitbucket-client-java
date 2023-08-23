package io.github.hmedioni.bitbucket.client.domain.comment;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermittedOperations {

    private boolean editable;

    private boolean deletable;

    private boolean transitionable;

}
