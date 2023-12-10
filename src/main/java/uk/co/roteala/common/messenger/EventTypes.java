package uk.co.roteala.common.messenger;

import java.io.Serializable;

public enum EventTypes implements Serializable {

    PEERS,
    BLOCK,
    STATECHAIN,
    ACCOUNT,
    MEMPOOL,
    TRANSACTION,
    NODESTATE,
    BLOCKHEADER,
    DEFAULT
}
