package io.github.hillelmed.bitbucket.client.domain.pullrequest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Person.
 */
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
