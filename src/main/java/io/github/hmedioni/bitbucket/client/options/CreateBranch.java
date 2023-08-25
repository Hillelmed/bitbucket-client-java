package io.github.hmedioni.bitbucket.client.options;


import lombok.*;
import org.springframework.lang.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateBranch {

    private String name;

    private String startPoint;

    // defaults to value of name if null
    @Nullable
    private String message;

}
