package uk.co.roteala.common.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.co.roteala.common.BaseModel;
import uk.co.roteala.common.Block;

@Data
@RequiredArgsConstructor
public class BlockMessage extends BaseModel implements Message {
    private final Block block;

    private String address;
    @Override
    public MessageTypes messageType() {
        return MessageTypes.BLOCK;
    }

    @Override
    public BaseModel getMessage() {
        return this.block;
    }

    @Override
    public MessageActions messageAction() {
        return null;
    }
}
