package uk.co.roteala.exceptions;

public class RPCException  extends ApplicationRuntimeException {
    public RPCException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RPCException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
