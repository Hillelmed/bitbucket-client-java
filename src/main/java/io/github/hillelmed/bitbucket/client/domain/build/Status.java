package io.github.hillelmed.bitbucket.client.domain.build;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Status {

    private long dateAdded;
    @Nullable
    private String description;
    private String key;
    @Nullable
    private String name;
    private StatusState state;
    private String url;

    /**
     * The enum Status state.
     */
    public enum StatusState {
        /**
         * Successful status state.
         */
        SUCCESSFUL,
        /**
         * Failed status state.
         */
        FAILED,
        /**
         * Inprogress status state.
         */
        INPROGRESS
    }

}
