package uk.co.roteala.common;

import lombok.*;
import uk.co.roteala.common.monetary.Coin;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ChainState extends BaseModel {
    private Integer lastBlockIndex;
    private Coin reward;
    private Integer target;
    private List<String> accounts;
    private Block getGenesisBlock = Block.builder()
            .difficulty(2)
            .index(0)
            .markleRoot("0000000000000000000000000000000000000000000000000000000000000000")
            .miner("Rotaru 'Roti' Andrei")
            .nonce("488")
            .numberOfBits(1252)
            .previousHash("0000000000000000000000000000000000000000000000000000000000000000")
            .forkHash("0000000000000000000000000000000000000000000000000000000000000000")
            .hash("00ca790ed9e0d0905ba05c0c27ad15c884d513d81cc86e8a722f90fd62eefa2c")
            .status(BlockStatus.MINED)
            .confirmations(1)
            .reward(Coin.ZERO)
            .timeStamp(1689347172622L)
            .transactions(new ArrayList<>())
            .version(0x16)
            .build();
}
