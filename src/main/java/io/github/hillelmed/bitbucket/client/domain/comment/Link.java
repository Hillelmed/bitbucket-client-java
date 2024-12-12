package io.github.hillelmed.bitbucket.client.domain.comment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Link.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Link {


    private String url;

    private String rel;

}
