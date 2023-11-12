package uk.co.roteala.common.messenger;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class Message implements Serializable {
    public String messageId;
    private MessageType messageType;
}
