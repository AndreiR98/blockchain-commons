package uk.co.roteala.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum Script implements Serializable {
    OP_DUP(0x76),
    OP_HASH160(0xa9),
    OP_EQUALVERIFY(0x88),
    OP_CHECKSIG(0xac);

    private Integer code;

    private static final Map<Integer, Script> VALUES = new HashMap<>();

    Script(Integer code) {
        this.code = code;
    }

    static {
        for (Script type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public Integer getCode() {
        return this.code;
    }

    public static Script valueOfCode(String code) {
        return VALUES.get(code);
    }
}
