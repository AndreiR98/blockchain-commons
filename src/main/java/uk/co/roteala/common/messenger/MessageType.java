package uk.co.roteala.common.messenger;

import java.io.Serializable;

public enum MessageType implements Serializable {
    KEY,
    CHUNK,
    EMPTY;
}
