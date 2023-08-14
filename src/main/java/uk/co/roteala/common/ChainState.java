package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.CoinConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("STATECHAIN")
public class ChainState extends BaseModel {
    private Integer lastBlockIndex;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin reward;
    private Integer target;
    private List<String> accounts;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin networkFees = Coin.valueOf(new BigDecimal("0.05"));
    private boolean allowEmptyMining;
    private Block genesisBlock = Block.builder()
            .confirmations(1)
            .status(BlockStatus.MINED)
            .forkHash("0000000000000000000000000000000000000000000000000000000000000000")
            .transactions(new ArrayList<>())
            .numberOfBits(1391)
            .header(BlockHeader.builder()
                    .previousHash("0000000000000000000000000000000000000000000000000000000000000000")
                    .hash("000c31ce73175eaf6274a0797c691dd48f45c1818be17ec677227d2afeef0604")
                    .markleRoot("0000000000000000000000000000000000000000000000000000000000000000")
                    .timeStamp(1691070213200l)
                    .nonce("76a")
                    .numberOfTransactions(0)
                    .minerAddress("Rotaru 'Roti' Andrei")
                    .version(0x33)
                    .index(0)
                    .difficulty(3)
                    .reward(Coin.valueOf(new BigDecimal("33")))
                    .build())
            .build();
}
