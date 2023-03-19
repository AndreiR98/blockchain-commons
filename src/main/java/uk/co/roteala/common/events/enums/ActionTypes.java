package uk.co.roteala.common.events.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum ActionTypes implements Serializable {

    FETCH("fetch"),
    DOWNLOAD("download"),
    ADD("add"),
    ADDVERIFY("addverify"),
    REMOVE("remove"),
    SYNC("synchronize"),
    MODIFY("modify");

    private String code;

    private static final Map<String, ActionTypes> VALUES = new HashMap<>();

    ActionTypes(String code) {
        this.code = code;
    }

    static {
        for (ActionTypes type : values()) {
            VALUES.put(type.code, type);
        }
    }

    public String getCode() {
        return this.code;
    }

    public static ActionTypes valueOfCode(String code) {
        return VALUES.get(code);
    }
}
