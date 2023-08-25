package io.github.hmedioni.bitbucket.client.domain.pullrequest;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Properties {

    private long openTaskCount;

    private long resolvedTaskCount;

}
