package io.github.hillelmed.bitbucket.client.domain.repository;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Hook.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hook {

    @Nullable
    private HookDetails details;

    private boolean enabled;

    private boolean configured;


}
