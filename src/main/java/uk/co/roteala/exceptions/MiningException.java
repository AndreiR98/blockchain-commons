package uk.co.roteala.exceptions;

public class MiningException extends ApplicationRuntimeException {
    public MiningException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MiningException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
