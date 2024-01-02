package uk.co.roteala.common.messenger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.netty.Connection;
import uk.co.roteala.common.*;
import uk.co.roteala.common.events.PeersContainer;
import uk.co.roteala.common.monetary.Vault;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "messageType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MessageKey.class, name = "KEY"),
        @JsonSubTypes.Type(value = MessageChunk.class, name = "CHUNK")
})
public abstract class Message implements Serializable {
    private String messageId;
    protected MessageType messageType;
    @JsonIgnore
    private HandlerType handler;
    private Connection owner;
}
