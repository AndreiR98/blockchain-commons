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
import java.util.Arrays;
import java.util.List;

@Slf4j
@UtilityClass
public class MessengerUtils {
    private final static Integer DEFAULT_CHUNK_SIZE = 2046;
    public static Message deserialize(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        try {
            String messageWrapperString = SerializationUtils.deserialize(bytes);
            ReferenceCountUtil.release(byteBuf);

            log.info("BytesHex:{}", messageWrapperString);

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
        // Provide a default message if deserialization fails
        // You may want to customize this based on your needs
        return new Message() {
        };
    }

    public List<ByteBuf> createChunks(BasicModel model, EventTypes eventTypes, EventActions eventActions) {
        byte[] availableBytes = SerializationUtils.serialize(modelToString(model));
        final int totalMessageSize = availableBytes.length;

        List<ByteBuf> byteBufs = new ArrayList<>();

        // Message uniqueID
        final String messageId = generateMessageId();

        int chunkNumber = 0;
        int offset = 0;

        MessageKey key = new MessageKey();
        key.setMessageId(messageId);
        key.setTotalBytes(totalMessageSize);
        key.setEventTypes(eventTypes);
        key.setAction(eventActions);

        while (offset < totalMessageSize) {
            // Create chunks
            MessageChunk chunk = new MessageChunk();
            chunk.setMessageId(messageId);
            chunk.setChunkNumber(chunkNumber);

            List<Byte> byteList = new ArrayList<>();

            int chunkSizeCounter = 0;

            // Add bytes to the chunk until reaching DEFAULT_CHUNK_SIZE or end of availableBytes
            while (offset < totalMessageSize && Unpooled.copiedBuffer(serializeMessage(chunk)).readableBytes() < DEFAULT_CHUNK_SIZE) {
                byteList.add(availableBytes[offset]);
                chunk.setHexedBytes(formatToBytes(byteList));


                //if(Unpooled.copiedBuffer(serializeMessage(chunk)).readableBytes() < DEFAULT_CHUNK_SIZE) {
                    offset++;
                    chunkSizeCounter++;
                //}
                chunk.setChunkSize(chunkSizeCounter);
            }

            byteBufs.add(Unpooled.copiedBuffer(serializeMessage(chunk)));
            System.out.println("Offset: " + offset + ", TotalMessageSize: " + chunk.getHexedBytes());
            chunkNumber++;
        }

        key.setTotalChunks(chunkNumber);

        byte[] keyBytes = serializeMessage(key);
        ByteBuf keyBuff = Unpooled.copiedBuffer(keyBytes);
        byteBufs.add(keyBuff);

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
