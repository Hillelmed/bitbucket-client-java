package io.github.hillelmed.bitbucket.client.domain.comment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Anchor.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anchor {

    private String diffType;
    private String path;
    @Nullable
    private Integer line;
    @Nullable
    private String lineType;
    @Nullable
    private String fileType;
    @Nullable
    private String srcPath;
    @Nullable
    private String fromHash;
    @Nullable
    private String toHash;

    /**
     * Effective anchor.
     *
     * @param line     the line
     * @param path     the path
     * @param lineType the line type
     * @param fileType the file type
     * @return the anchor
     */
    public static Anchor effective(@Nullable Integer line, String path, @Nullable LineType lineType, @Nullable FileType fileType) {
        String strLineType = lineType != null ? lineType.toString() : null;
        String strFileType = fileType != null ? fileType.toString() : null;
        return new Anchor(DiffType.EFFECTIVE.toString(), path, line, strLineType, strFileType, null, null, null);
    }

    /**
     * General file anchor.
     *
     * @param path     the path
     * @param srcPath  the src path
     * @param fromHash the from hash
     * @param toHash   the to hash
     * @return the anchor
     */
    public static Anchor generalFile(String path, @Nullable String srcPath, @Nullable String fromHash,
                                     @Nullable String toHash) {
        return new Anchor(DiffType.COMMIT.toString(), path, null, null, null, srcPath, fromHash, toHash);
    }

    /**
     * File line commit anchor.
     *
     * @param line     the line
     * @param path     the path
     * @param srcPath  the src path
     * @param lineType the line type
     * @param fileType the file type
     * @param fromHash the from hash
     * @param toHash   the to hash
     * @return the anchor
     */
    public static Anchor fileLineCommit(@Nullable Integer line, String path, @Nullable String srcPath,
                                        @Nullable LineType lineType, @Nullable FileType fileType, @Nullable String fromHash,
                                        @Nullable String toHash) {
        String strLineType = lineType != null ? lineType.toString() : null;
        String strFileType = fileType != null ? fileType.toString() : null;
        return new Anchor(DiffType.COMMIT.toString(), path, line, strLineType, strFileType, srcPath, fromHash, toHash);
    }

    /**
     * File line range anchor.
     *
     * @param line     the line
     * @param path     the path
     * @param srcPath  the src path
     * @param lineType the line type
     * @param fileType the file type
     * @param fromHash the from hash
     * @param toHash   the to hash
     * @return the anchor
     */
    public static Anchor fileLineRange(@Nullable Integer line, String path, @Nullable String srcPath,
                                       @Nullable LineType lineType, @Nullable FileType fileType,
                                       @Nullable String fromHash, @Nullable String toHash) {
        String strLineType = lineType != null ? lineType.toString() : null;
        String strFileType = fileType != null ? fileType.toString() : null;
        return new Anchor(DiffType.RANGE.toString(), path, line, strLineType, strFileType, srcPath, fromHash, toHash);
    }

    /**
     * The enum Diff type.
     */
    public enum DiffType {
        /**
         * Range diff type.
         */
        RANGE,
        /**
         * Commit diff type.
         */
        COMMIT,
        /**
         * Effective diff type.
         */
        EFFECTIVE
    }

    /**
     * The enum Line type.
     */
    public enum LineType {
        /**
         * Added line type.
         */
        ADDED,
        /**
         * Removed line type.
         */
        REMOVED,
        /**
         * Context line type.
         */
        CONTEXT
    }

    /**
     * The enum File type.
     */
    public enum FileType {
        /**
         * From file type.
         */
        FROM,
        /**
         * To file type.
         */
        TO
    }

}
