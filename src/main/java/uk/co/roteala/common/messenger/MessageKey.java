package uk.co.roteala.common.messenger;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("KEY")
public class MessageKey extends Message{
    private Integer totalChunks;
    private Integer totalBytes;
    private EventActions action;
    private EventTypes eventTypes;

    @Override
    public void setMessageType(MessageType messageType) {
        super.setMessageType(MessageType.KEY);
    }

    @Override
    public MessageType getMessageType() {
        return super.getMessageType();
    }

    @Override
    public String getMessageId() {
        return super.getMessageId();
    }
}
