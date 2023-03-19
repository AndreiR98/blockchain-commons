package uk.co.roteala.common.events.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum SubjectTypes implements Serializable {

    TRANSACTION("transaction"),
    TRANSACTION_FIELD("transactionF"),
    TRANSACTION_MEMPOOL("mempool"),
    TRANSACTION_MEMPOOL_FIELD("mempoolF"),
    BLOCK("block"),
    BLOCK_FIELD("blockF"),
    PEER("peer"),
    WALLET("wallet");

    private String code;

    private static final Map<String, SubjectTypes> VALUES = new HashMap<>();

    SubjectTypes(String code) {
        this.code = code;
    }

    static {
        for (SubjectTypes type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public String getCode() {
        return this.code;
    }

    public static SubjectTypes valueOfCode(String code) {
        return VALUES.get(code);
    }
}
