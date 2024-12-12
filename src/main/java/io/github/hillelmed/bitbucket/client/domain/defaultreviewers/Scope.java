package io.github.hillelmed.bitbucket.client.domain.defaultreviewers;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Scope.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Scope {

    private ScopeType type;
    private Long resourceId;

    /**
     * The enum Scope type.
     */
    public enum ScopeType {
        /**
         * Repository scope type.
         */
        REPOSITORY,
        /**
         * Project scope type.
         */
        PROJECT
    }

}
