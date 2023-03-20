package uk.co.roteala.common.events;

import lombok.*;
import org.springframework.util.SerializationUtils;
import reactor.netty.NettyOutbound;
import uk.co.roteala.common.BaseEmptyModel;
import uk.co.roteala.common.events.enums.ActionTypes;
import uk.co.roteala.common.events.enums.Structure;
import uk.co.roteala.common.events.enums.SubjectTypes;
import uk.co.roteala.common.events.transformer.ScriptTransformerSupplier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Serializable, Messenger {
    private ActionTypes action;
    private SubjectTypes subject;
    private String extra;

    @Override
    public byte[] serialize() {
        return SerializationUtils.serialize(this);
    }

    @Override
    public List<BaseEmptyModel> flatTransform(ScriptTransformerSupplier<Event, List<BaseEmptyModel>> transformerSupplier) {
        return transformerSupplier.get().transform(this);
    }
}
