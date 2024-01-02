package uk.co.roteala.common.messenger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import uk.co.roteala.security.utils.HashingService;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("CHUNK")
public class MessageChunk extends Message{
    private Integer chunkNumber;
    private String hexedBytes;
    private Integer chunkSize;

    @Override
    public void setMessageType(MessageType messageType) {
        super.setMessageType(MessageType.CHUNK);
    }

    @Override
    public void setHandler(HandlerType handlerType) {
        super.setHandler(handlerType);
    }

    @Override
    public MessageType getMessageType() {
        return super.getMessageType();
    }

    @Override
    public String getMessageId() {
        return super.getMessageId();
    }

    @JsonIgnore
    public byte[] toBytes() {
        return HashingService
                .hexStringToByteArray(this.hexedBytes);
    }
}
