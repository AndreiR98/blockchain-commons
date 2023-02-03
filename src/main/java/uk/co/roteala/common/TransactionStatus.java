package uk.co.roteala.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum TransactionStatus implements Serializable {

    PENDING("001"),
    VALIDATED("002"),
    PROCESSED("003"),
    SUCCESS("004");

    private String code;

    private static final Map<String, TransactionStatus> VALUES = new HashMap<>();

    TransactionStatus(String code) {
        this.code = code;
    }

    static {
        for (TransactionStatus type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public String getCode() {
        return this.code;
    }

    public static TransactionStatus valueOfCode(String code) {
        return VALUES.get(code);
    }
}
