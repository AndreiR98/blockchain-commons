package uk.co.roteala.common.events;

import uk.co.roteala.common.AccountType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum MessageTypes implements Serializable {

    PEERS("peers"),
    BLOCK("block"),
    STATECHAIN("statechain"),
    ACCOUNT("account"),
    MEMPOOL("mempool");

    private String code;

    private static final Map<String, MessageTypes> VALUES = new HashMap<>();

    MessageTypes(String code) {
        this.code = code;
    }

    static {
        for (MessageTypes type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public String getCode() {
        return this.code;
    }

    public static MessageTypes valueOfCode(String code) {
        return VALUES.get(code);
    }
}
