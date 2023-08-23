package io.github.hmedioni.bitbucket.client.domain.common;


import lombok.*;
import org.springframework.lang.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veto {

    @Nullable
    public String summaryMessage;

    @Nullable
    public String detailedMessage;

}
