package uk.co.roteala.common.events;

import uk.co.roteala.common.AccountType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum MessageTypes implements Serializable {

    PEERS,
    BLOCK,
    STATECHAIN,
    ACCOUNT,
    MEMPOOL,
    TRANSACTION
}
