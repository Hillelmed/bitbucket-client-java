package io.github.hillelmed.bitbucket.client.domain.branch;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * The enum Branch restriction enum type.
 */
@Getter
public enum BranchRestrictionEnumType {

    /**
     * The Fast forward only.
     */
    FAST_FORWARD_ONLY("Rewriting history",
            "fast-forward-only",
            "prevents history rewrites on the specified branch(es) - for example by a force push or rebase."),

    /**
     * The No deletes.
     */
    NO_DELETES("Deletion",
            "no-deletes",
            "prevents branch and tag deletion"),

    /**
     * The Pull request only.
     */
    PULL_REQUEST_ONLY("Changes without a pull request",
            "pull-request-only",
            "prevents pushing changes directly to the specified branch(es); changes are allowed only with a pull request"),

    /**
     * The Read only.
     */
    READ_ONLY("All changes",
            "read-only",
            "prevents pushes to the specified branch(es) and restricts creating new"
                    + " branches matching the specified branch(es) or pattern");

    private final String name;
    private final String apiName;
    private final String description;

    BranchRestrictionEnumType(final String name, final String apiName, final String description) {
        this.name = name;
        this.apiName = apiName;
        this.description = description;
    }

    /**
     * Convert value from Api to enum.
     *
     * @param apiName ApiName
     * @return value branch restriction enum type
     */
    public static BranchRestrictionEnumType fromValue(final String apiName) {
        for (final BranchRestrictionEnumType enumType : BranchRestrictionEnumType.values()) {
            if (enumType.getApiName().equals(apiName)) {
                return enumType;
            }
        }
        throw new IllegalArgumentException("Value " + apiName + " is not a legal BranchPermission type");
    }

    /**
     * Gets api name.
     *
     * @return the api name
     */
    @JsonValue
    public String getApiName() {
        return apiName;
    }

    @Override
    public String toString() {
        return this.getApiName();
    }
}
