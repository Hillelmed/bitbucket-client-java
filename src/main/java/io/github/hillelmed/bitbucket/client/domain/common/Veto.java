package io.github.hillelmed.bitbucket.client.domain.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.io.Serializable;

/**
 * The type Veto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veto implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * The Summary message.
     */
    @Nullable
    public String summaryMessage;

    /**
     * The Detailed message.
     */
    @Nullable
    public String detailedMessage;

}
