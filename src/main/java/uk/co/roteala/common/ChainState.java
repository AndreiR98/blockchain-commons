package uk.co.roteala.common;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ChainState extends BaseEmptyModel {
    private Integer lastBlockIndex;
    private String minerKey;
    private BigInteger target;
    private BigInteger nonce;
    private List<UTXO> utxo;
}
