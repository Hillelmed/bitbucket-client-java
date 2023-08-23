package io.github.hmedioni.bitbucket.client.domain.insights;


import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.lang.*;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Annotation {

    @Nullable
    public String reportKey;
    @Nullable
    public String externalId;
    public int line;
    @Nullable
    public String link;
    public String message;
    @Nullable
    public String path;
    @Nullable
    public AnnotationSeverity severity;
    @Nullable
    public AnnotationType type;

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
