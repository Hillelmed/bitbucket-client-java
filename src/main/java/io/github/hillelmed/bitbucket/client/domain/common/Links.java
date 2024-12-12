package io.github.hillelmed.bitbucket.client.domain.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * The type Links.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Links {

    private List<Map<String, String>> clone;

    private List<Map<String, String>> self;

}
