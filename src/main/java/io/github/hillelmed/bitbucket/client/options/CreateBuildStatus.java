package io.github.hillelmed.bitbucket.client.options;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Create build status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBuildStatus {

    private STATE state;
    private String key;
    @Nullable
    private String name;
    private String url;
    @Nullable
    private String description;

    /**
     * The enum State.
     */
    public enum STATE {
        /**
         * Successful state.
         */
        SUCCESSFUL,
        /**
         * Failed state.
         */
        FAILED,
        /**
         * Inprogress state.
         */
        INPROGRESS
    }

}
