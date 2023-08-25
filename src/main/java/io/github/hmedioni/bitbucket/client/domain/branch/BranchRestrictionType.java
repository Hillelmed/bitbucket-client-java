package io.github.hmedioni.bitbucket.client.domain.branch;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchRestrictionType {

    private String id;
    private String name;

    public static BranchRestrictionType create(final Matcher.MatcherId matcherId) {
        return new BranchRestrictionType(matcherId.getTypeId(), matcherId.getTypeName());
    }

}
