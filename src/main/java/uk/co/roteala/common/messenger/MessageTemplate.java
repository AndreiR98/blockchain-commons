package uk.co.roteala.common.messenger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vertx.core.net.NetSocket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.netty.Connection;
import uk.co.roteala.common.BasicModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageTemplate  {
    public static final MessageTemplate DEFAULT = emptyMessage();
    private EventActions eventAction;
    private BasicModel message;
    private EventTypes eventType;
    private NetSocket owner;
    private ReceivingGroup group;
    private List<NetSocket> withOut;
    private HandlerType handler;
    private String messageId;

    //@Override
    @JsonIgnore
    public List<NetSocket> getWithOut() {
        return Objects.requireNonNullElseGet(this.withOut, ArrayList::new);
    }

    public static final MessageTemplate emptyMessage() {
        return MessageTemplate.builder()
                .message(null)
                .owner(null)
                .eventAction(EventActions.DISCARD)
                .eventType(null)
                .build();
    }
}
