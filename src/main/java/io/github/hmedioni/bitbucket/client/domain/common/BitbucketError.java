package io.github.hmedioni.bitbucket.client.domain.common;

import lombok.*;
import org.springframework.lang.*;

import java.io.*;
import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BitbucketError implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;


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
