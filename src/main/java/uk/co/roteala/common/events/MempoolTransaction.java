package uk.co.roteala.common.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.co.roteala.common.BaseModel;
import uk.co.roteala.common.PseudoTransaction;

@Data
@RequiredArgsConstructor
public class MempoolTransaction extends BaseModel implements Message{
    private final PseudoTransaction transaction;
    @Override
    public MessageTypes messageType() {
        return MessageTypes.MEMPOOL;
    }

    @Override
    public BaseModel getMessage() {
        return this.transaction;
    }
}
