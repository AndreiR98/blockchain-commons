package uk.co.roteala.exceptions;

public class BlockException extends ApplicationRuntimeException {
    public BlockException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BlockException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
