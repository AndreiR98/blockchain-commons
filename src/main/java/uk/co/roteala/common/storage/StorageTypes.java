package uk.co.roteala.common.storage;

import uk.co.roteala.common.BlockStatus;

import java.util.HashMap;
import java.util.Map;

public enum StorageTypes {
    STATE("stateTrieStorage"),
    MEMPOOL("mempoolStorage"),
    PEERS("peersStorage"),
    BLOCKCHAIN("stateBlockchain");

    private String name;

    private static final Map<String, StorageTypes> VALUES = new HashMap<>();

    StorageTypes(String name) {
        this.name = name;
    }

    static {
        for (StorageTypes type : values()) {
            VALUES.put(type.name, type);
        }
    }

    public String getName() {
        return this.name;
    }

    public static StorageTypes valueOfName(String name) {
        return VALUES.get(name);
    }
}
