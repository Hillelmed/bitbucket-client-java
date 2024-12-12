package io.github.hillelmed.bitbucket.client.domain.file;


import io.github.hillelmed.bitbucket.client.domain.common.PageEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;


/**
 * The type Line page.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinePage extends PageEmpty {

    private List<Line> lines;
    @Nullable
    private List<Blame> blame;

}
