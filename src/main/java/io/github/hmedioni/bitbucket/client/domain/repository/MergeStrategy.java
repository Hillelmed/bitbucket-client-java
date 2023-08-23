package io.github.hmedioni.bitbucket.client.domain.repository;


import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.lang.*;


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

    @Getter
    public enum MergeStrategyId {

        FF("ff"),
        FF_ONLY("ff-only"),
        NO_FF("no-ff"),
        REBASE_NO_FF("rebase-no-ff"),
        REBASE_FF_ONLY("rebase-ff-only"),
        SQUASH("squash"),
        SQUASH_FF_ONLY("squash-ff-only");

        private final String apiName;

        MergeStrategyId(final String apiName) {
            this.apiName = apiName;
        }

        /**
         * Convert value from Api to enum.
         *
         * @param apiName ApiName
         * @return value
         */
        public static MergeStrategyId fromValue(final String apiName) {
            for (final MergeStrategyId enumType : MergeStrategyId.values()) {
                if (enumType.getApiName().equals(apiName)) {
                    return enumType;
                }
            }
            throw new IllegalArgumentException("Value " + apiName + " is not a legal MergeStrategy type");
        }

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
