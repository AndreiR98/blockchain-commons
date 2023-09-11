package uk.co.roteala.common.events;

import lombok.Builder;
import lombok.Data;
import reactor.netty.Connection;
import uk.co.roteala.common.BaseModel;

@Data
@Builder
public class MessageTemplate implements Message{
    private MessageTypes type;
    private MessageActions messageAction;
    private Connection connection;

    private final BaseModel content;

    private ValidationType verified;

    private String address;

    @Override
    public MessageTypes getMessageType() {
        return this.type;
    }

    @Override
    public BaseModel getMessage() {
        return this.content;
    }
}
