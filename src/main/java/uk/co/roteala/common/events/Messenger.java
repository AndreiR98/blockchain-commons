package uk.co.roteala.common.events;

import reactor.netty.NettyOutbound;

public interface Messenger {
    MessengerScript parseMessage();

    byte[] serialize();

    void process(Processor<Event> e, NettyOutbound outbound);
}
