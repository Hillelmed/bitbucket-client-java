package io.github.hmedioni.bitbucket.client.domain.file;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    @Nullable
    private String text;

    @Nullable
    private String type;

}
