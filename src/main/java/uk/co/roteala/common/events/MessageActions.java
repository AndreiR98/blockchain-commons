package uk.co.roteala.common.events;

public enum MessageActions {
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
    DELETE,
    /**
     * Directly add to the ledger, instant flush
     * */
    APPEND,
    /**
     * Syncronize the node
     * */
    REQUEST_SYNC,


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
    CONNECT_PEER


}
