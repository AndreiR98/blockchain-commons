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
            .nonce("2cc")
            .numberOfBits(1252)
            .previousHash("0000000000000000000000000000000000000000000000000000000000000000")
            .forkHash("0000000000000000000000000000000000000000000000000000000000000000")
            .hash("007bb9f4538ed56e2bd8820025d098c91aaf9eee97565e5a685b35cbd6822a81")
            .status(BlockStatus.MINED)
            .confirmations(1)
            .reward(Coin.ZERO)
            .timeStamp(1689943713679L)
            .transactions(new ArrayList<>())
            .version(0x16)
            .build();
}
