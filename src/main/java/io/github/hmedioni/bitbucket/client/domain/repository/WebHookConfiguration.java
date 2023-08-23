package io.github.hmedioni.bitbucket.client.domain.repository;


import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebHookConfiguration {

    @Nullable
    public String secret;

}
