package io.github.hmedioni.bitbucket.client.domain.branch;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Matcher {

    public String id;
    public String displayId;
    public BranchRestrictionType type;
    public Boolean active;

    public Matcher(MatcherId matcherId, Boolean active) {
        this.id = matcherId.getId();
        this.displayId = matcherId.getName();
        this.type = BranchRestrictionType.create(matcherId);
        this.active = active;
    }

    @Getter
    public enum MatcherId {

        RELEASE("RELEASE", "Release", "MODEL_CATEGORY", "Branching model category"),
        DEVELOPMENT("Development", "Development", "MODEL_BRANCH", "Branching model branch"),
        MASTER("Production", "Production", "MODEL_BRANCH", "Branching model branch"),
        ANY_REF("ANY_REF_MATCHER_ID", "ANY_REF_MATCHER_ID", "ANY_REF", "Any branch"),
        ANY("any", "any", "ANY_REF", "ANY_REF");

        private final String id;
        private final String name;
        private final String typeId;
        private final String typeName;

        MatcherId(final String id,
                  final String name,
                  final String typeId,
                  final String typeName) {
            this.id = id;
            this.name = name;
            this.typeId = typeId;
            this.typeName = typeName;
        }

    }

}
