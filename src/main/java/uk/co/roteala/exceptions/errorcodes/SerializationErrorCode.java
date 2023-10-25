package uk.co.roteala.exceptions.errorcodes;

import uk.co.roteala.exceptions.ErrorCode;

public enum SerializationErrorCode implements ErrorCode {
    SERIALIZATION_FAILED("json.001"),
    DESERIALIZATION_FAILED("json.002");

    SerializationErrorCode(String key) {
        this.key = key;
    }

    final String key;

    @Override
    public String getKey() {
        return key;
    }
}
