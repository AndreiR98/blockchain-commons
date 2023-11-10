package uk.co.roteala.common.messenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.SerializationUtils;
import uk.co.roteala.common.events.MessageTemplate;

public interface AssemblerSupplier {
    default Message mapper(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        try {
            String messageWrapperString = SerializationUtils.deserialize(bytes);
            ReferenceCountUtil.release(byteBuf);
            // Convert bytes to String

            ObjectMapper objectMapper = new ObjectMapper();
            Message messageWrapper = objectMapper.readValue(messageWrapperString, Message.class);

            MessageChunk.MessageChunkBuilder messageChunkBuilder = MessageChunk.builder()
                    .messageId(messageWrapper.getMessageId())
                    .chunkNumber(messageWrapper.getChunkNumber())
                    .messageType(messageWrapper.getMessageType())
                    .chunkNumber(messageWrapper.getSequenceChunkNumber())
                    .payload(messageWrapper.getPayload());

            return messageChunkBuilder.build();

        } catch (Exception e) {
            return defaultMessage();
        }
    }

    default MessageChunk defaultMessage() {
        return MessageChunk.builder()
                .messageId(null)
                .chunkNumber(null)
                .messageType(MessageType.EMPTY)
                .payload(null)
                .build();
    }
}
