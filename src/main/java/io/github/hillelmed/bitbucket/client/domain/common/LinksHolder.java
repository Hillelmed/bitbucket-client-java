package io.github.hillelmed.bitbucket.client.domain.common;

import org.springframework.lang.Nullable;

/**
 * This interface should NOT be applied to "option" like classes and/or used
 * in instances where this is applied to outgoing http traffic. This interface
 * should ONLY be used for classes modeled after incoming http traffic.
 */
public interface LinksHolder {
    /**
     * Links links.
     *
     * @return the links
     */
    @Nullable
    Links links();
}
