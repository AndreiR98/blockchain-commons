package uk.co.roteala.common.messenger;

import lombok.Builder;
import lombok.Data;
import uk.co.roteala.common.events.MessageActions;

@Data
@Builder
public class MessageKey {
    private String messageKey;
    private MessageType messageType;
    private Integer totalChunks;
    private Integer totalBytes;
    private MessageActions action;
}
