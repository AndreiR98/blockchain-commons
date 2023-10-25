package uk.co.roteala.common.events;

import lombok.Builder;
import lombok.Data;
import reactor.netty.Connection;
import uk.co.roteala.common.BasicModel;

@Data
@Builder
public class MessageTemplate implements Message{
    private MessageTypes type;
    private MessageActions messageAction;
    private Connection connection;

    private final BasicModel content;

    private ValidationType verified;

    private String address;

    @Override
    public MessageTypes getMessageType() {
        return this.type;
    }

    @Override
    public BasicModel getMessage() {
        return this.content;
    }
}
