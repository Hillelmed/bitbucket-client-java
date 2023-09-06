package io.github.hmedioni.bitbucket.client.domain.sshkey;


import lombok.*;
import org.springframework.lang.*;


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

    public static Key create(String text, String label, Integer bitLength, Integer expiryDays, String algorithmType) {
        return new Key(null, null, null, text, label, bitLength, expiryDays, algorithmType);
    }
}
