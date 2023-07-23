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
     * Response type to the verification asked by broker or nodes
     * This will be processed diffrently;
     * When response comes true +1 to the confirmations if confirmations > threshold append block adn send message to all to do the same
     * */
    RESPONSE_TO_VERIFY,

    VERIFY_WITH_BROKER
}
