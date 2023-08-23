package io.github.hmedioni.bitbucket.client.domain.file;


import io.github.hmedioni.bitbucket.client.domain.pullrequest.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blame {

    private Author author;

    private String authorTimestamp;

    private String commitHash;

    private String displayCommitHash;

    private String commitId;

    private String commitDisplayId;

    private String fileName;

    private int lineNumber;

    private int spannedLines;

}
