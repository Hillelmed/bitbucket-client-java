package io.github.hillelmed.bitbucket.client.domain.pullrequest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Parents.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parents {

    /**
     * The Display id.
     */
    @Nullable
    public String displayId;
    private String id;

}
