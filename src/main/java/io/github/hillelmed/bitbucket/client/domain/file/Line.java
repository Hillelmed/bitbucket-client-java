package io.github.hillelmed.bitbucket.client.domain.file;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Line.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    @Nullable
    private String text;

    @Nullable
    private String type;

}
