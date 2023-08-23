package io.github.hmedioni.bitbucket.client.domain.branch;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchRestrictionType {

    public String id;
    public String name;

    public static BranchRestrictionType create(final Matcher.MatcherId matcherId) {
        return new BranchRestrictionType(matcherId.getTypeId(), matcherId.getTypeName());
    }

}
