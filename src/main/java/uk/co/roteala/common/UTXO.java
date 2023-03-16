package uk.co.roteala.common;


import lombok.*;
import uk.co.roteala.common.monetary.Coin;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UTXO implements Serializable {
    private Coin value;
    private String ref;
    private String hash;
    private String script;
    private long index;
    private long height;
    private boolean coinbase;
    private String address;
}
