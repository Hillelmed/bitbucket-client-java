package io.github.hmedioni.bitbucket.client.domain.repository;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hook {

    @Nullable
    private HookDetails details;

    private boolean enabled;

    private boolean configured;


}
