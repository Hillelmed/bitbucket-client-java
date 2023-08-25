package io.github.hmedioni.bitbucket.client.domain.common;


import lombok.*;
import org.springframework.lang.*;

import java.io.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veto implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    @Nullable
    public String summaryMessage;

    @Nullable
    public String detailedMessage;

}
