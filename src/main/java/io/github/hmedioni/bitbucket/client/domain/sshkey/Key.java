package io.github.hmedioni.bitbucket.client.domain.sshkey;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Key {

    @Nullable
    public Long id;

    public String text;

    public String label;
}
