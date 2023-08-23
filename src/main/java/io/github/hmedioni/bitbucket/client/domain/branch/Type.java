package io.github.hmedioni.bitbucket.client.domain.branch;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Type {

    private TypeId id;

    @Nullable
    private String displayName;

    private String prefix;

    private boolean enabled;


    public enum TypeId {
        BUGFIX,
        FEATURE,
        HOTFIX,
        RELEASE
    }

}
