package uk.co.roteala.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum AccountType implements Serializable {

    GENESIS("G"),
    PERSONAL("P"),
    COMPANY("C"),
    BUSINESS("B");

    private String code;

    private static final Map<String, AccountType> VALUES = new HashMap<>();

    AccountType(String code) {
        this.code = code;
    }

    static {
        for (AccountType type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public String getCode() {
        return this.code;
    }

    public static AccountType valueOfCode(String code) {
        return VALUES.get(code);
    }
}
