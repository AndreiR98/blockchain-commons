package uk.co.roteala.common.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import reactor.netty.Connection;
import uk.co.roteala.common.BasicModel;


@Data
@RequiredArgsConstructor
public class BlockMessage implements Message {
    private Connection connection;

    private MessageActions messageAction;

    private final BasicModel message;

    private ValidationType verified;

    private String address;
    @Override
    public MessageTypes getMessageType() {
        return MessageTypes.BLOCK;
    }
}
