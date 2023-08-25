package io.github.hmedioni.bitbucket.client.domain.sshkey;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Key {

    @Nullable
    private Long id;
    private String text;
    private String label;
}
