package uk.co.roteala.exceptions;

public class MessageSerializationException extends ApplicationRuntimeException {
    public MessageSerializationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MessageSerializationException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
