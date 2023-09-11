package uk.co.roteala.exceptions.errorcodes;

import uk.co.roteala.exceptions.ErrorCode;

public enum SyncErrorCode implements ErrorCode {
    NO_CONNECTIONS("sync.001"),
    SYNC_NOT_POSSIBLE("sync.002");

    SyncErrorCode(String key) {
        this.key = key;
    }

    final String key;

    @Override
    public String getKey() {
        return key;
    }
}
