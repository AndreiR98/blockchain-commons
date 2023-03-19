package uk.co.roteala.common.events;

import lombok.*;
import org.springframework.util.SerializationUtils;
import reactor.netty.NettyOutbound;
import uk.co.roteala.common.events.enums.ActionTypes;
import uk.co.roteala.common.events.enums.Structure;
import uk.co.roteala.common.events.enums.SubjectTypes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Serializable, Messenger {
    private String message;

    @Override
    public MessengerScript parseMessage() {
        Map<Structure, BaseEvent> m = parser(this.message);

        return MessengerScript.builder()
                .action((Action) m.get(Structure.ACTION))
                .subject((Subject) m.get(Structure.SUBJECT))
                .extra((Extra) m.get(Structure.EXTRA))
                .build();
    }

    @Override
    public byte[] serialize() {
        return SerializationUtils.serialize(this.message);
    }

    private Map<Structure, BaseEvent> parser(String m){
        Map<Structure, BaseEvent> map = new HashMap<>();

        String[] structure = m.split(" ");

        map.put(Structure.ACTION, Action.builder().action(ActionTypes.valueOfCode(structure[0])).build());
        map.put(Structure.SUBJECT, Subject.builder().subject(SubjectTypes.valueOfCode(structure[1])).build());
        map.put(Structure.EXTRA, Extra.builder().extra(structure[2]).build());

        return map;
    }

    @Override
    public void process(Processor<Event> e, NettyOutbound outbound) {
    }
}
