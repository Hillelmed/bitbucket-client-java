package io.github.hmedioni.bitbucket.client.domain.file;


import io.github.hmedioni.bitbucket.client.domain.common.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinePage extends PageEmpty {

    private List<Line> lines;
    @Nullable
    private List<Blame> blame;

}
