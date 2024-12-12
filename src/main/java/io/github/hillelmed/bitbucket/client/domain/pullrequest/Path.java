package io.github.hillelmed.bitbucket.client.domain.pullrequest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Path.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Path {

    private List<String> components;

    private String parent;

    private String name;

    @Nullable
    private String extension;

    @Override
    public String toString() {
        if (parent != null && !parent.isEmpty()) {
            return parent + "/" + name;
        }
        return name;
    }
}
