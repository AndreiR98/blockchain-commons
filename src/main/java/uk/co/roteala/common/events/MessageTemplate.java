package uk.co.roteala.common.events;

import lombok.Builder;
import lombok.Data;
import reactor.netty.Connection;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.common.messenger.Message;

@Builder
public class MessageTemplate extends Message {
}
