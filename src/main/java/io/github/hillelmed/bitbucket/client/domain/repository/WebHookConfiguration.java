package io.github.hillelmed.bitbucket.client.domain.repository;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Web hook configuration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebHookConfiguration {

    /**
     * The Secret.
     */
    @Nullable
    public String secret;

}
