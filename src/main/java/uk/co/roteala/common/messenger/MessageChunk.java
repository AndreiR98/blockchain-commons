package uk.co.roteala.common.messenger;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageChunk extends Message{
    private String messageId;
    private MessageType messageType;
    private byte[] payload;
    private Integer chunkNumber;
}
