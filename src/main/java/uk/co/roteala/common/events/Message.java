package uk.co.roteala.common.events;

import uk.co.roteala.common.BaseModel;

import java.io.Serializable;

public interface Message extends Serializable {
    MessageTypes messageType();

    BaseModel getMessage();
}
