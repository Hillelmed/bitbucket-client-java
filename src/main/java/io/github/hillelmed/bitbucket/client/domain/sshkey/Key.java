package io.github.hillelmed.bitbucket.client.domain.sshkey;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * The type Key.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Key {

    @Nullable
    private Long id;
    @Nullable
    private String createdDate;
    @Nullable
    private String lastAuthenticated;
    private String text;
    private String label;
    private Integer bitLength;
    private Integer expiryDays;
    private String algorithmType;

    /**
     * Create key.
     *
     * @param text          the text
     * @param label         the label
     * @param bitLength     the bit length
     * @param expiryDays    the expiry days
     * @param algorithmType the algorithm type
     * @return the key
     */
    public static Key create(String text, String label, Integer bitLength, Integer expiryDays, String algorithmType) {
        return new Key(null, null, null, text, label, bitLength, expiryDays, algorithmType);
    }
}
