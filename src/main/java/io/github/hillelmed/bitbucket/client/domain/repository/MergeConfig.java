package io.github.hillelmed.bitbucket.client.domain.repository;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Merge config.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergeConfig {

    private MergeStrategy defaultStrategy;
    private List<MergeStrategy> strategies;
    private MergeConfigType type;
    @Nullable
    private Integer commitSummaries;

    /**
     * The enum Merge config type.
     */
    public enum MergeConfigType {
        /**
         * Repository merge config type.
         */
        REPOSITORY,
        /**
         * Default merge config type.
         */
        DEFAULT,
        /**
         * Project merge config type.
         */
        PROJECT
    }
}
