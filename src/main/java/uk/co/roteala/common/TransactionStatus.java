package uk.co.roteala.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum TransactionStatus implements Serializable {

    PENDING(1),
    VALIDATED(2),
    PROCESSED(3),
    SUCCESS(4),
    LOCKED(5);

    private Integer code;

    private static final Map<Integer, TransactionStatus> VALUES = new HashMap<>();

    TransactionStatus(Integer code) {
        this.code = code;
    }

    static {
        for (TransactionStatus type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public Integer getCode() {
        return this.code;
    }

    public static TransactionStatus valueOfCode(Integer code) {
        return VALUES.get(code);
    }
}
