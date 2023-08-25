package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import lombok.*;
import org.springframework.lang.*;

import java.util.*;


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
