package uk.co.roteala.common.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.*;
import org.apache.commons.lang3.SerializationUtils;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.common.messenger.EventActions;
import uk.co.roteala.common.messenger.EventTypes;
import uk.co.roteala.exceptions.MessageSerializationException;
import uk.co.roteala.exceptions.errorcodes.MessageSerializationErrCode;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageWrapper implements Serializable {
    private EventTypes type;
    private BasicModel content;
    private ValidationType verified;
    private EventActions action;

    @JsonIgnore
    public ByteBuf serialize() {
        try {
            ObjectMapper mapper = new ObjectMapper();

            return Unpooled.copiedBuffer(
                    SerializationUtils.serialize(
                            mapper.writeValueAsString(this)));
        } catch (Exception e) {
            throw new MessageSerializationException(MessageSerializationErrCode.SERIALIZATION_FAILED);
        }
    }
}
