package uk.co.roteala.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum BlockStatus implements Serializable {

    PENDING("001"),
    MINED("002");

    private String code;

    private static final Map<String, BlockStatus> VALUES = new HashMap<>();

    BlockStatus(String code) {
        this.code = code;
    }

    static {
        for (BlockStatus type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public String getCode() {
        return this.code;
    }

    public static BlockStatus valueOfCode(String code) {
        return VALUES.get(code);
    }
}
