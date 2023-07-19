package uk.co.roteala.common.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import reactor.netty.Connection;
import uk.co.roteala.common.AccountModel;
import uk.co.roteala.common.BaseModel;

@Data
@RequiredArgsConstructor
public class PeerMessage extends BaseModel implements Message {
    private Connection connection;

    private MessageActions messageAction;

    private final BaseModel message;

    private final boolean verified;
    @Override
    public MessageTypes getMessageType() {
        return MessageTypes.PEERS;
    }

    @Override
    public BaseModel getMessage() {
        return this.message;
    }

    @Override
    public boolean isVerified() {
        return this.verified;
    }
}
