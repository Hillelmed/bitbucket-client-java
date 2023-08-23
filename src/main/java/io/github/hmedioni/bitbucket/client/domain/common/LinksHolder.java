package io.github.hmedioni.bitbucket.client.domain.common;

import org.springframework.lang.*;

/**
 * This interface should NOT be applied to "option" like classes and/or used
 * in instances where this is applied to outgoing http traffic. This interface
 * should ONLY be used for classes modeled after incoming http traffic.
 */
public interface LinksHolder {
    @Nullable
    Links links();
}
