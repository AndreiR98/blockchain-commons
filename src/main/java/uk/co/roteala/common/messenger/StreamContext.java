package uk.co.roteala.common.messenger;

import reactor.netty.NettyInbound;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface StreamContext {
    ConcurrentHashMap<String, NettyInbound> sourceSuppliers();
}
