package uk.co.roteala.common.messenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.security.utils.HashingService;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class MessengerUtils {
    private final static Integer DEFAULT_CHUNK_SIZE = 2046;
    public static Message deserialize(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        log.info("Bytes:{}", byteBuf.readableBytes());

        try {
            String messageWrapperString = SerializationUtils.deserialize(bytes);
            ReferenceCountUtil.release(byteBuf);

            ObjectMapper objectMapper = new ObjectMapper();
            Message messageWrapper = objectMapper.readValue(messageWrapperString, Message.class);

            if (messageWrapper instanceof MessageKey) {
                return (MessageKey) messageWrapper;
            } else if (messageWrapper instanceof MessageChunk) {
                return (MessageChunk) messageWrapper;
            } else {
                // Handle the case where the deserialized message is neither MessageKey nor MessageChunk
                return defaultMessage();
            }

        } catch (Exception e) {
            return defaultMessage();
        }
    }

    private static Message defaultMessage() {
        return new Message() {
        };
    }

    public List<ByteBuf> createChunks(MessageTemplate template) {
        byte[] availableBytes = SerializationUtils.serialize(modelToString(template.getMessage()));
        final int totalMessageSize = availableBytes.length;

        List<ByteBuf> byteBufs = new ArrayList<>();

        // Message uniqueID
        final String messageId = generateMessageId();

        int chunkNumber = 0;
        int offset = 0;

        MessageKey key = new MessageKey();
        key.setMessageId(messageId);
        key.setTotalBytes(totalMessageSize);
        key.setEventType(template.getEventType());
        key.setEventAction(template.getEventAction());

        while (offset < totalMessageSize) {
            MessageChunk chunk = new MessageChunk();
            chunk.setMessageId(messageId);
            chunk.setChunkNumber(chunkNumber);

            List<Byte> byteList = new ArrayList<>();
            int chunkSizeCounter = 0;

            while (offset < totalMessageSize) {
                byteList.add(availableBytes[offset]);
                offset++;
                chunkSizeCounter++;

                chunk.setHexedBytes(formatToBytes(byteList));
                chunk.setChunkSize(chunkSizeCounter);

                byte[] serializedChunk = serializeMessage(chunk);
                ByteBuf buffer = Unpooled.copiedBuffer(serializedChunk);
                int size = buffer.readableBytes();
                buffer.release(); // Release the ByteBuf

                if (size >= DEFAULT_CHUNK_SIZE) {
                    break;
                }
            }

            byteBufs.add(Unpooled.copiedBuffer(serializeMessage(chunk)));
            chunkNumber++;
        }

        key.setTotalChunks(chunkNumber);

        byte[] keyBytes = serializeMessage(key);
        ByteBuf keyBuff = Unpooled.copiedBuffer(keyBytes);
        byteBufs.add(keyBuff);
        //keyBuff.release();

        return byteBufs;
    }

    private String formatToBytes(List<Byte> bytesList) {
        byte[] bytesData = new byte[bytesList.size()];

        for (int i = 0; i < bytesList.size(); i++) {
            bytesData[i] = bytesList.get(i);
        }

        return HashingService.bytesToHexString(bytesData);
    }

    private byte[] serializeMessage(Message message) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            return SerializationUtils.serialize(mapper.writeValueAsString(message));
        } catch (Exception e) {
            log.error("Failed to serialize message:{}", message);
            return null;
        }
    }

    //Generate 12 bytes SHA256 hash
    private String generateMessageId() {
        SecureRandom secureRandom = new SecureRandom();

        byte[] randomBytes = new byte[12];
        secureRandom.nextBytes(randomBytes);

        return HashingService.bytesToHexString(HashingService
                .sha256Hash(randomBytes));
    }

    private static String modelToString(BasicModel model) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.writeValueAsString(model);
        } catch (Exception e) {
            log.error("Failed to serialize model:{}", model);
            return null;
        }
    }
}
