package io.github.hmedioni.bitbucket.client.domain.repository;


import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergeConfig {

    private MergeStrategy defaultStrategy;
    private List<MergeStrategy> strategies;
    private MergeConfigType type;
    @Nullable
    private Integer commitSummaries;

    public enum MergeConfigType {
        REPOSITORY,
        DEFAULT,
        PROJECT
    }
}
