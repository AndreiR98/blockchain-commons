package uk.co.roteala.common.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.co.roteala.common.BaseModel;
import uk.co.roteala.common.PseudoTransaction;

@Data
@RequiredArgsConstructor
public class MempoolTransaction extends BaseModel implements Message{
    private final PseudoTransaction transaction;

    private String address;
    @Override
    public MessageTypes messageType() {
        return MessageTypes.MEMPOOL;
    }

    @Override
    public BaseModel getMessage() {
        return this.transaction;
    }

    @Override
    public MessageActions messageAction() {
        return null;
    }

//    @Override
//    public void setAddress(String address) {
//        this.address = address;
//    }
}
