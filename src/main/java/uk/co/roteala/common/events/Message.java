package uk.co.roteala.common.events;

import reactor.netty.Connection;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.common.messenger.EventActions;
import uk.co.roteala.common.messenger.EventTypes;

import java.io.Serializable;

public interface Message extends Serializable {
    EventTypes getMessageType();

    BasicModel getMessage();

    EventActions getMessageAction();

    Connection getConnection();

    String getAddress();

    void setConnection(Connection connection);

    ValidationType getVerified();

    void setVerified(ValidationType v);

    void setMessageAction(EventActions action);

    void setAddress(String address);
}
