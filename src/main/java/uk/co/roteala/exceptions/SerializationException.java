package uk.co.roteala.exceptions;
public class SerializationException extends ApplicationRuntimeException {
    public SerializationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SerializationException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
