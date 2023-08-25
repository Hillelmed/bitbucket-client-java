package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parents {

    private String id;

    @Nullable
    public String displayId;

}
