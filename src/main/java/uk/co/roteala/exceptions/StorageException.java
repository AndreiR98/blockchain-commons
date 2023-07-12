package uk.co.roteala.exceptions;

public class StorageException extends ApplicationRuntimeException {
    public StorageException(ErrorCode errorCode) {
        super(errorCode);
    }

    public StorageException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
