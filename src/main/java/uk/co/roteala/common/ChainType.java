package uk.co.roteala.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum ChainType implements Serializable {

    BROKERS("33"),
    SINGLE("05"),
    NODES("003");

    private String code;

    private static final Map<String, ChainType> VALUES = new HashMap<>();

    ChainType(String code) {
        this.code = code;
    }

    static {
        for(ChainType type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public String getCode() {
        return this.code;
    }

    public static ChainType valueOfCode(String code) {
        return VALUES.get(code);
    }
}
