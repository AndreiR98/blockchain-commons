package uk.co.roteala.common.events;

import reactor.netty.Connection;
import uk.co.roteala.common.BaseModel;

import java.io.Serializable;

public interface Message extends Serializable {
    MessageTypes getMessageType();

    BaseModel getMessage();

    MessageActions getMessageAction();

    Connection getConnection();

    String getAddress();

    void setConnection(Connection connection);

    ValidationType getVerified();

    void setVerified(ValidationType v);

    void setMessageAction(MessageActions action);

    void setAddress(String address);
}
