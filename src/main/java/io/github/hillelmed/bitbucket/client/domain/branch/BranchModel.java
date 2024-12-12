package io.github.hillelmed.bitbucket.client.domain.branch;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Branch model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchModel {

    @Nullable
    private Branch development;

    @Nullable
    private Branch production;

    private List<Type> types;

}
