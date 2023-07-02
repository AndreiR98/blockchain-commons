package uk.co.roteala.exceptions;

public class TransactionException extends ApplicationRuntimeException {
    public TransactionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TransactionException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
