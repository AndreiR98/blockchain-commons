package uk.co.roteala.common.storage;

import java.util.HashMap;
import java.util.Map;

public enum ColumnFamilyTypes {
    TRANSACTIONS("transactions"),
    BLOCKS("blocks"),
    ACCOUNTS("accounts"),
    STATE("state"),
    NODE("node");

    private String name;

    private static final Map<String, ColumnFamilyTypes> VALUES = new HashMap<>();

    ColumnFamilyTypes(String name) {
        this.name = name;
    }

    static {
        for (ColumnFamilyTypes type : values()) {
            VALUES.put(type.name, type);
        }
    }

    public String getName() {
        return this.name;
    }

    public static ColumnFamilyTypes valueOfName(String name) {
        return VALUES.get(name);
    }
}
