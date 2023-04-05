package uk.co.roteala.storage;

import uk.co.roteala.common.ChainStatus;

import java.util.HashMap;
import java.util.Map;

public enum StorageType {

    WALLET("W"),
    MEMPOOL("M"),
    BLOCKS("B"),
    TX("T"),
    CHAIN("C"),
    PEERS("P");

    private String code;

    private static final Map<String, StorageType> VALUES = new HashMap<>();

    StorageType(String code) {
        this.code = code;
    }

    static {
        for (StorageType type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public String getCode() {
        return this.code;
    }

    public static StorageType valueOfCode(String code) {
        return VALUES.get(code);
    }
}
