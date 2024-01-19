package uk.co.roteala.common.messenger;

import java.io.Serializable;

public enum HandlerType implements Serializable {
    BROKER,
    CLIENT,
    SERVER;
}
