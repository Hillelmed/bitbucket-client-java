package io.github.hillelmed.bitbucket.client.domain.branch;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Type.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Type {

    private TypeId id;

    @Nullable
    private String displayName;

    private String prefix;

    private boolean enabled;


    /**
     * The enum Type id.
     */
    public enum TypeId {
        /**
         * Bugfix type id.
         */
        BUGFIX,
        /**
         * Feature type id.
         */
        FEATURE,
        /**
         * Hotfix type id.
         */
        HOTFIX,
        /**
         * Release type id.
         */
        RELEASE
    }

}
