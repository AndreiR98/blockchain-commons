package uk.co.roteala.utils;

import lombok.experimental.UtilityClass;
import uk.co.roteala.common.*;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.Vault;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Constants {
    public final String DEFAULT_HASH = "0000000000000000000000000000000000000000000000000000000000000000";

    public final String DEFAULT_STATE_NAME = "state_chain";

    public final String DEFAULT_NODE_STATE = "node_state";

    public static final String DEFAULT_NETWORK_VERSION = "0x1";

    public static final Block GENESIS_BLOCK = Block.builder()
            .confirmations(1)
            .status(BlockStatus.MINED)
            .forkHash(DEFAULT_HASH)
            .transactions(new ArrayList<>())
            .numberOfBits(1391)
            .header(BlockHeader.builder()
                    .previousHash(DEFAULT_HASH)
                    .hash("000c31ce73175eaf6274a0797c691dd48f45c1818be17ec677227d2afeef0604")
                    .markleRoot(DEFAULT_HASH)
                    .timeStamp(1691070213200l)
                    .nonce("76a")
                    .numberOfTransactions(0)
                    .minerAddress("Rotaru 'Roti' Andrei")
                    .version(0x33)
                    .index(0)
                    .difficulty(3)
                    .build())
            .build();

    public static final ChainState GENESIS_STATE = ChainState.builder()
            .lastBlockIndex(0)
            .reward(new BigInteger("14d1120d7b160000", 16))
            .networkFees(new BigInteger("b1a2bc2ec50000", 16))
            .target(4)
            .chainId(7331)
            .netVersion(1)
            .allowEmptyMining(false)
            .build();

    public static final NodeState NODE_STATE = NodeState.builder()
            .lastBlockIndex(0)
            .remainingBlocks(0)
            .updatedAt(System.currentTimeMillis())
            .build();
}
