package uk.co.roteala.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum BlockStatus implements Serializable {

    PROPOSED(0),
    PENDING(1),
    CONFIRMED(2),
    MINED(3),
    FAILED(4);

    private Integer code;

    private static final Map<Integer, BlockStatus> VALUES = new HashMap<>();

    BlockStatus(Integer code) {
        this.code = code;
    }

    static {
        for (BlockStatus type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public Integer getCode() {
        return this.code;
    }

    public static BlockStatus valueOfCode(Integer code) {
        return VALUES.get(code);
    }
}
