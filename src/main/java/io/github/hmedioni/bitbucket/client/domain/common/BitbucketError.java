package io.github.hmedioni.bitbucket.client.domain.common;

import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BitbucketError {

    @Nullable
    private String context;

    @Nullable
    private String message;

    @Nullable
    private String exceptionName;

    private boolean conflicted;

    @Nullable
    private List<Veto> vetoes;

}
