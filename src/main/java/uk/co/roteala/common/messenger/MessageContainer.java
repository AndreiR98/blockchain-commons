package uk.co.roteala.common.messenger;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MessageContainer implements Serializable {
    private MessageKey key;
    private List<MessageChunk> chunks;
}
