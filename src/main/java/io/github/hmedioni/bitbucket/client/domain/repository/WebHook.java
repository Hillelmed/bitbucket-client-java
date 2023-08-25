package io.github.hmedioni.bitbucket.client.domain.repository;


import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.lang.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebHook {

    @Nullable
    private String id;

    @Nullable
    private String name;

    private long createdDate;

    private long updatedDate;

    @Nullable
    private List<EventType> events;

    @Nullable
    private WebHookConfiguration configuration;

    @Nullable
    private String url;

    private boolean active;

    @Getter
    public enum EventType {
        REPO_COMENT_ADDED("repo:comment:added"),
        REPO_COMENT_EDITED("repo:comment:edited"),
        REPO_COMENT_DELETED("repo:comment:deleted"),

        REPO_FORKED("repo:forked"),
        REPO_CHANGED("repo:refs_changed"),
        REPO_MODIFIED("repo:modified"),

        PR_COMMENT_ADDED("pr:comment:added"),
        PR_COMMENT_EDITED("pr:comment:edited"),
        PR_COMMENT_DELETED("pr:comment:deleted"),

        PR_REVIEWER_UPDATE("pr:reviewer:updated"),
        PR_REVIEWER_UNAPPROVED("pr:reviewer:unapproved"),
        PR_REVIEWER_APPROVED("pr:reviewer:approved"),
        PR_REVIEWER_NEEDSWORK("pr:reviewer:needs_work"),

        PR_DELETED("pr:deleted"),
        PR_MERGED("pr:merged"),
        PR_MODIFIED("pr:modified"),
        PR_DECLINED("pr:declined"),
        PR_OPENED("pr:opened"),
        PR_FROM_REF_UPDATED("pr:from_ref_updated");

        private final String apiName;

        EventType(final String apiName) {
            this.apiName = apiName;
        }

        /**
         * Convert value from Api to enum.
         *
         * @param apiName ApiName
         * @return value
         */
        public static EventType fromValue(final String apiName) {
            for (final EventType enumType : EventType.values()) {
                if (enumType.getApiName().equals(apiName)) {
                    return enumType;
                }
            }
            throw new IllegalArgumentException("Value " + apiName + " is not a legal EventType type");
        }

        @JsonValue
        private String getApiName() {
            return apiName;
        }

        @Override
        public String toString() {
            return this.getApiName();
        }
    }

}
