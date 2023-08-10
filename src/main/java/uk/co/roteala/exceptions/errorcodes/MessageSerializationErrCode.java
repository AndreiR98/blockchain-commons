package uk.co.roteala.exceptions.errorcodes;

import uk.co.roteala.exceptions.ErrorCode;

public enum MessageSerializationErrCode implements ErrorCode {
    SERIALIZATION_FAILED("message.001"),
    DESERIALIZATION_FAILED("message.002");
    MessageSerializationErrCode(String key) {
        this.key = key;
    }

    final String key;

    @Override
    public String getKey() {
        return key;
    }
}
