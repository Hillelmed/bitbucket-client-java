package io.github.hmedioni.bitbucket.client.domain.defaultreviewers;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Scope {

    private ScopeType type;
    private Long resourceId;

    public enum ScopeType {
        REPOSITORY,
        PROJECT
    }

}
