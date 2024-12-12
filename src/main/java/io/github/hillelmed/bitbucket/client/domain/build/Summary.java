package io.github.hillelmed.bitbucket.client.domain.build;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Summary.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Summary {

    private long failed;

    private long inProgress;

    private long successful;

}
