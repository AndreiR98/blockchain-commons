package uk.co.roteala.exceptions.errorcodes;

import uk.co.roteala.exceptions.ErrorCode;

public enum MiningErrorCode implements ErrorCode {
    BLOCK_NOT_FOUND("mining.001"),
    MINED_BLOCK_EMPTY("mining.002"),
    PREVIOUS_HASH("mining.003"),
    PSEUDO_MATCH("mining.004"),
    IN_BETWEEN("mining.005"),
    OPERATION_FAILED("mining.006"),
    ALREADY_EXISTS("mining.007"),
    PSEUDO_TRANSACTION_NOT_FOUND("mining.008");


    MiningErrorCode(String key) {
        this.key = key;
    }

    final String key;

    @Override
    public String getKey() {
        return key;
    }
}
