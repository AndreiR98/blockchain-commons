package uk.co.roteala.common.events;

import reactor.netty.NettyOutbound;
import uk.co.roteala.common.BaseEmptyModel;
import uk.co.roteala.common.events.transformer.ScriptTransformerSupplier;

import java.util.List;

public interface Messenger {
    byte[] serialize();

    List<BaseEmptyModel> flatTransform(ScriptTransformerSupplier<Event, List<BaseEmptyModel>> transformerSupplier);
}
