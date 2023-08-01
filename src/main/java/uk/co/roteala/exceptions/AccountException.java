package uk.co.roteala.exceptions;

public class AccountException  extends ApplicationRuntimeException {
    public AccountException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AccountException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
