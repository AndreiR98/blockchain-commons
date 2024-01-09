package uk.co.roteala.exceptions.errorcodes;

import uk.co.roteala.exceptions.ErrorCode;

public enum StorageErrorCode implements ErrorCode {
    TRANSACTION_NOT_FOUND("storage.001"),
    ACCOUNT_NOT_FOUND("storage.002"),
    MEMPOOL_NOT_FOUND("storage.003"),
    BLOCK_NOT_FOUND("storage.004"),
    STATE_NOT_FOUND("storage.005"),
    PEER_NOT_FOUND("storage.006"),
    STORAGE_NOT_FOUND("storage.007"),
    STORAGE_FAILED("storage.008"),
    SERIALIZATION("storage.009"),
    DATA_ALREADY("storage.010"),
    HANDLER_NOT_FOUND("storage.011"),
    IMMUTABILITY("storage.012"),
    MODIFY_NON_EXISTANT("storage.013");

    StorageErrorCode(String key) {
        this.key = key;
    }

    final String key;

    @Override
    public String getKey() {
        return key;
    }
}
