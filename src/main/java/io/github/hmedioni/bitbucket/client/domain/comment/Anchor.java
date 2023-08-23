package io.github.hmedioni.bitbucket.client.domain.comment;


import lombok.*;
import org.springframework.lang.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anchor {

    public String diffType;
    public String path;
    @Nullable
    public Integer line;
    @Nullable
    public String lineType;
    @Nullable
    public String fileType;
    @Nullable
    public String srcPath;
    @Nullable
    public String fromHash;
    @Nullable
    public String toHash;

    public static Anchor effective(@Nullable Integer line, String path, @Nullable LineType lineType, @Nullable FileType fileType) {
        String strLineType = lineType != null ? lineType.toString() : null;
        String strFileType = fileType != null ? fileType.toString() : null;
        return new Anchor(DiffType.EFFECTIVE.toString(), path, line, strLineType, strFileType, null, null, null);
    }

    public static Anchor generalFile(String path, @Nullable String srcPath, @Nullable String fromHash,
                                     @Nullable String toHash) {
        return new Anchor(DiffType.COMMIT.toString(), path, null, null, null, srcPath, fromHash, toHash);
    }

    public static Anchor fileLineCommit(@Nullable Integer line, String path, @Nullable String srcPath,
                                        @Nullable LineType lineType, @Nullable FileType fileType, @Nullable String fromHash,
                                        @Nullable String toHash) {
        String strLineType = lineType != null ? lineType.toString() : null;
        String strFileType = fileType != null ? fileType.toString() : null;
        return new Anchor(DiffType.COMMIT.toString(), path, line, strLineType, strFileType, srcPath, fromHash, toHash);
    }

    public static Anchor fileLineRange(@Nullable Integer line, String path, @Nullable String srcPath,
                                       @Nullable LineType lineType, @Nullable FileType fileType,
                                       @Nullable String fromHash, @Nullable String toHash) {
        String strLineType = lineType != null ? lineType.toString() : null;
        String strFileType = fileType != null ? fileType.toString() : null;
        return new Anchor(DiffType.COMMIT.toString(), path, line, strLineType, strFileType, srcPath, fromHash, toHash);
    }

    public enum DiffType {
        RANGE,
        COMMIT,
        EFFECTIVE
    }

    public enum LineType {
        ADDED,
        REMOVED,
        CONTEXT
    }

    public enum FileType {
        FROM,
        TO
    }

}
