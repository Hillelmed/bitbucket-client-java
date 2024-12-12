package io.github.hillelmed.bitbucket.client.domain.insights;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Annotation.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Annotation {

    @Nullable
    private String reportKey;
    @Nullable
    private String externalId;
    private int line;
    @Nullable
    private String link;
    private String message;
    @Nullable
    private String path;
    @Nullable
    private AnnotationSeverity severity;
    @Nullable
    private AnnotationType type;

    /**
     * Instantiates a new Annotation.
     *
     * @param externalId the external id
     * @param line       the line
     * @param link       the link
     * @param message    the message
     * @param path       the path
     * @param severity   the severity
     * @param type       the type
     */
    public Annotation(@Nullable String externalId, int line, @Nullable String link, String message, @Nullable String path, @Nullable AnnotationSeverity severity, @Nullable AnnotationType type) {
        this.reportKey = null;
        this.externalId = externalId;
        this.line = line;
        this.link = link;
        this.message = message;
        this.path = path;
        this.severity = severity;
        this.type = type;
    }

    /**
     * The enum Annotation severity.
     */
    public enum AnnotationSeverity {
        /**
         * Low annotation severity.
         */
        LOW,
        /**
         * Medium annotation severity.
         */
        MEDIUM,
        /**
         * High annotation severity.
         */
        HIGH
    }

    /**
     * The enum Annotation type.
     */
    public enum AnnotationType {
        /**
         * Vulnerability annotation type.
         */
        VULNERABILITY,
        /**
         * Code smell annotation type.
         */
        CODE_SMELL,
        /**
         * Bug annotation type.
         */
        BUG
    }

}
