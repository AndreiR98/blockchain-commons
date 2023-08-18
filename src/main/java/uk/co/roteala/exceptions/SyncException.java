package uk.co.roteala.exceptions;

public class SyncException extends ApplicationRuntimeException {
    public SyncException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SyncException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
