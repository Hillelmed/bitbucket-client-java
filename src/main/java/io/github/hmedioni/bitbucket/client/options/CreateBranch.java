package io.github.hmedioni.bitbucket.client.options;


import lombok.*;
import org.springframework.lang.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateBranch {

    public String name;

    public String startPoint;

    // defaults to value of name if null
    @Nullable
    public String message;

}
