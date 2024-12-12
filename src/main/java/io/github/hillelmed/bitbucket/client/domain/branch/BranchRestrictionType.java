package io.github.hillelmed.bitbucket.client.domain.branch;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Branch restriction type.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchRestrictionType {

    private String id;
    private String name;

    /**
     * Create branch restriction type.
     *
     * @param matcherId the matcher id
     * @return the branch restriction type
     */
    public static BranchRestrictionType create(final Matcher.MatcherId matcherId) {
        return new BranchRestrictionType(matcherId.getTypeId(), matcherId.getTypeName());
    }

}
