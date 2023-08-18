package uk.co.roteala.common.events;

public enum MessageActions {
    DEFAULT,
    /**
     * Ask the node to verify the specific model
     * */
    VERIFY,
    /**
     * Modify either account balances or number of confirmations
     * */
    MODIFY,
    /**
     * Delete specific model
     * */
    DISCARD,
    /**
     * Directly add to the ledger, instant flush
     * */
    APPEND,
    /**
     * Syncronize the node
     * */
    REQUEST_SYNC,
    REQUEST,


    /**
     * MINERS MESSAGE WITH MINED BLOCK
     * */
    MINED_BLOCK,

    /**
     * VERIFIED_MINED_BLOCK
     * */
    VERIFIED_MINED_BLOCK,

    /**
     * APPEND_MINED_BLOCK
     * */
    APPEND_MINED_BLOCK,


    /**
     * PEERS
     * */
    STORE_PEER,

    /**
     * CONNECT_PEER
     * */
    CONNECT_PEER,

    /**
     * Asking broker for more connections
     * */
    NO_CONNECTIONS

}
