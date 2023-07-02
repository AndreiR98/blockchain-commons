package uk.co.roteala.exceptions.errorcodes;

import uk.co.roteala.exceptions.ErrorCode;

public enum StorageErrorCode implements ErrorCode {
    TRANSACTION_NOT_FOUND("storage.001"),
    ACCOUNT_NOT_FOUND("storage.002");

    StorageErrorCode(String key) {
        this.key = key;
    }

    final String key;

    @Override
    public String getKey() {
        return key;
    }
}
