package uk.co.roteala.common.monetary;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum CoinSign implements Serializable {

    PLUS("+"),
    MINUS("-");

    private String code;

    private static final Map<String, CoinSign> VALUES = new HashMap<>();

    CoinSign(String code) {
        this.code = code;
    }

    static {
        for (CoinSign type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public String getCode() {
        return this.code;
    }

    public static CoinSign valueOfCode(String code) {
        return VALUES.get(code);
    }
}
