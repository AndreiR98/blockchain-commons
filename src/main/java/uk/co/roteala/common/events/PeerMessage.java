package uk.co.roteala.common.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.co.roteala.common.BaseModel;

@Data
@RequiredArgsConstructor
public class PeerMessage extends BaseModel implements Message{
    private final PeersContainer peersContainer;

    private String address;
    @Override
    public MessageTypes messageType() {
        return MessageTypes.PEERS;
    }

    @Override
    public BaseModel getMessage() {
        return this.peersContainer;
    }

    @Override
    public MessageActions messageAction() {
        return null;
    }
}
