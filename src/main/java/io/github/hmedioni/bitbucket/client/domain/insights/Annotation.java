package io.github.hmedioni.bitbucket.client.domain.insights;


import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.lang.*;


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

    public enum AnnotationSeverity {
        LOW,
        MEDIUM,
        HIGH
    }

    public enum AnnotationType {
        VULNERABILITY,
        CODE_SMELL,
        BUG
    }

}
