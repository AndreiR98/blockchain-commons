package uk.co.roteala.common.events.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum Structure implements Serializable {

    ACTION("ACTION"),
    SUBJECT("SUBJECT"),
    EXTRA("EXTRA");

    private String code;

    private static final Map<String, Structure> VALUES = new HashMap<>();

    Structure(String code) {
        this.code = code;
    }

    static {
        for (Structure type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public String getCode() {
        return this.code;
    }

    public static Structure valueOfCode(String code) {
        return VALUES.get(code);
    }
}
