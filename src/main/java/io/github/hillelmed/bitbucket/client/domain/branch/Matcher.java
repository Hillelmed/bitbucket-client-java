package io.github.hillelmed.bitbucket.client.domain.branch;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The type Matcher.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Matcher {

    private String id;
    private String displayId;
    private BranchRestrictionType type;
    private Boolean active;

    /**
     * Instantiates a new Matcher.
     *
     * @param matcherId the matcher id
     * @param active    the active
     */
    public Matcher(MatcherId matcherId, Boolean active) {
        this.id = matcherId.getId();
        this.displayId = matcherId.getName();
        this.type = BranchRestrictionType.create(matcherId);
        this.active = active;
    }

    /**
     * The enum Matcher id.
     */
    @Getter
    public enum MatcherId {

        /**
         * The Release.
         */
        RELEASE("RELEASE", "Release", "MODEL_CATEGORY", "Branching model category"),
        /**
         * The Development.
         */
        DEVELOPMENT("Development", "Development", "MODEL_BRANCH", "Branching model branch"),
        /**
         * The Master.
         */
        MASTER("Production", "Production", "MODEL_BRANCH", "Branching model branch"),
        /**
         * The Any ref.
         */
        ANY_REF("ANY_REF_MATCHER_ID", "ANY_REF_MATCHER_ID", "ANY_REF", "Any branch"),
        /**
         * Any matcher id.
         */
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
