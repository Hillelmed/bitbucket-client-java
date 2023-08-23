package io.github.hmedioni.bitbucket.client.domain.build;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Summary {

    private long failed;

    private long inProgress;

    private long successful;

}
