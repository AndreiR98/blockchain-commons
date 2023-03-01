package uk.co.roteala.net;

import uk.co.roteala.common.BlockStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum OPCODES implements Serializable {
    reqPeers(0x1a),
    sendPeers(0x1b);

    private int code;

    private static final Map<Integer, OPCODES> VALUES = new HashMap<>();

    OPCODES(Integer code) {
        this.code = code;
    }

    static {
        for (OPCODES type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public Integer getCode() {
        return this.code;
    }

    public static OPCODES valueOfCode(Integer code) {
        return VALUES.get(code);
    }
}
