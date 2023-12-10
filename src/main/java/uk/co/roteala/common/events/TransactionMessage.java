package uk.co.roteala.common.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import reactor.netty.Connection;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.common.messenger.EventActions;
import uk.co.roteala.common.messenger.EventTypes;

@Data
@RequiredArgsConstructor
public class TransactionMessage implements Message {
    private Connection connection;

    private EventActions messageAction;

    private final BasicModel message;

    private ValidationType verified;

    private String address;
    @Override
    public EventTypes getMessageType() {
        return EventTypes.TRANSACTION;
    }
}
