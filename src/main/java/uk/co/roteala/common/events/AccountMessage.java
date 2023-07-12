package uk.co.roteala.common.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.co.roteala.common.AccountModel;
import uk.co.roteala.common.BaseModel;

@Data
@RequiredArgsConstructor
public class AccountMessage extends BaseModel implements Message{

    private final AccountModel account;

    private String address;
    @Override
    public MessageTypes messageType() {
        return MessageTypes.ACCOUNT;
    }

    @Override
    public BaseModel getMessage() {
        return this.account;
    }

    @Override
    public MessageActions messageAction() {
        return null;
    }
}
