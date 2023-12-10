package uk.co.roteala.exceptions.errorcodes;

import uk.co.roteala.exceptions.ErrorCode;

public enum RPCErrorCode implements ErrorCode {
    MALFORMED_PAYLOAD("rpc.001"),
    DESERIALIZATION_FAILED("rpc.002");

    RPCErrorCode(String key) {
        this.key = key;
    }

    final String key;

    @Override
    public String getKey() {
        return key;
    }
}

