package io.github.hillelmed.bitbucket.client.domain.repository;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Merge strategy.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergeStrategy {

    @Nullable
    private String description;
    @Nullable
    private Boolean enabled;
    @Nullable
    private String flag;
    private MergeStrategyId id;
    @Nullable
    private String name;

    /**
     * The enum Merge strategy id.
     */
    @Getter
    public enum MergeStrategyId {

        /**
         * Ff merge strategy id.
         */
        FF("ff"),
        /**
         * Ff only merge strategy id.
         */
        FF_ONLY("ff-only"),
        /**
         * No ff merge strategy id.
         */
        NO_FF("no-ff"),
        /**
         * Rebase no ff merge strategy id.
         */
        REBASE_NO_FF("rebase-no-ff"),
        /**
         * Rebase ff only merge strategy id.
         */
        REBASE_FF_ONLY("rebase-ff-only"),
        /**
         * Squash merge strategy id.
         */
        SQUASH("squash"),
        /**
         * Squash ff only merge strategy id.
         */
        SQUASH_FF_ONLY("squash-ff-only");

        private final String apiName;

        MergeStrategyId(final String apiName) {
            this.apiName = apiName;
        }

        /**
         * Convert value from Api to enum.
         *
         * @param apiName ApiName
         * @return value merge strategy id
         */
        public static MergeStrategyId fromValue(final String apiName) {
            for (final MergeStrategyId enumType : MergeStrategyId.values()) {
                if (enumType.getApiName().equals(apiName)) {
                    return enumType;
                }
            }
            throw new IllegalArgumentException("Value " + apiName + " is not a legal MergeStrategy type");
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

}
