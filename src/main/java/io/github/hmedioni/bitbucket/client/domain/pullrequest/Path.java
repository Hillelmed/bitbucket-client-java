package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Path {

    public List<String> components;

    public String parent;

    public String name;

    @Nullable
    public String extension;

    @Override
    public String toString() {
        if (parent != null && !parent.isEmpty()) {
            return parent + "/" + name;
        }
        return name;
    }
}
