package io.github.hillelmed.bitbucket.client.domain.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


/**
 * The type Bitbucket error.
 */
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
