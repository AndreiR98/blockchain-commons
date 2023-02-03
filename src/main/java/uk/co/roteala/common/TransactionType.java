package uk.co.roteala.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum TransactionType implements Serializable {

    TRANSACTION("001"),
    CHARGEBACK("002"),
    ADJUSTMENT("003"),
    REFUND("004");

    private String code;

    private static final Map<String, TransactionType> VALUES = new HashMap<>();

    TransactionType(String code) {
        this.code = code;
    }

    static {
        for(TransactionType type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public String getCode() {
        return this.code;
    }

    public static TransactionType valueOfCode(String code) {
        return VALUES.get(code);
    }
}
