package uk.co.roteala.common.events;

import reactor.netty.Connection;
import uk.co.roteala.common.BaseModel;

import java.io.Serializable;

public interface Message extends Serializable {
    MessageTypes getMessageType();

    BaseModel getMessage();

    MessageActions getMessageAction();

    Connection getConnection();

    void setConnection(Connection connection);

    boolean isVerified();

    void setVerified(boolean v);

    void setMessageAction(MessageActions action);
}
