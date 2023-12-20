package uk.co.roteala.common.messenger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.netty.Connection;
import uk.co.roteala.common.BasicModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageTemplate  {
    public static final MessageTemplate DEFAULT = emptyMessage();
    private EventActions eventAction;
    private BasicModel message;
    private EventTypes eventType;
    private Connection owner;
    private ReceivingGroup group;
    private String messageId;

    public static final MessageTemplate emptyMessage() {
        return MessageTemplate.builder()
                .message(null)
                .owner(null)
                .eventAction(EventActions.DISCARD)
                .eventType(null)
                .build();
    }
}
