package uk.co.roteala.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum ChainStatus implements Serializable {

    ACTIVE("V"),
    INACTIVE("X"),
    MERGING("M");

    private String code;

    private static final Map<String, ChainStatus> VALUES = new HashMap<>();

    ChainStatus(String code) {
        this.code = code;
    }

    static {
        for(ChainStatus type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public String getCode() {
        return this.code;
    }

    public static ChainStatus valueOfCode(String code) {
        return VALUES.get(code);
    }
}
