package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private User user;

    @Nullable
    private String role;

    private boolean approved;

    @Nullable
    private String status;

}
