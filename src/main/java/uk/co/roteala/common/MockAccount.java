package uk.co.roteala.common;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MockAccount {
    private String address;
    private String wif;
    private String privateKey;

    private String scriptKey;
}
