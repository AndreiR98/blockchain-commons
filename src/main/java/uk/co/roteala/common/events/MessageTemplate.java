package uk.co.roteala.common.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.netty.Connection;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.common.messenger.EventActions;
import uk.co.roteala.common.messenger.MessageType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageTemplate  {
    public static final MessageTemplate DEFAULT = emptyMessage();
    private EventActions actions;
    private BasicModel message;
    private MessageType type;
    private Connection owner;

    public static final MessageTemplate emptyMessage() {
        return MessageTemplate.builder()
                .message(null)
                .owner(null)
                .actions(EventActions.DISCARD)
                .type(null)
                .build();
    }
}
