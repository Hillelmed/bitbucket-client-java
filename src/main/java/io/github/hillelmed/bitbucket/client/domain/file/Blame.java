package io.github.hillelmed.bitbucket.client.domain.file;


import io.github.hillelmed.bitbucket.client.domain.pullrequest.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The type Blame.
 */
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
