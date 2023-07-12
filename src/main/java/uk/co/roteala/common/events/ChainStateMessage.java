package uk.co.roteala.common.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.co.roteala.common.BaseModel;
import uk.co.roteala.common.ChainState;

@Data
@RequiredArgsConstructor
public class ChainStateMessage extends BaseModel implements Message{
    private final ChainState message;

    private String address;
    @Override
    public MessageTypes messageType() {
        return MessageTypes.STATECHAIN;
    }

    @Override
    public BaseModel getMessage() {
        return this.message;
    }

    @Override
    public MessageActions messageAction() {
        return null;
    }
}
