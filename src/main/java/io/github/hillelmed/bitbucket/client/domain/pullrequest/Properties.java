package io.github.hillelmed.bitbucket.client.domain.pullrequest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Properties.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Properties {

    private long openTaskCount;

    private long resolvedTaskCount;

}
