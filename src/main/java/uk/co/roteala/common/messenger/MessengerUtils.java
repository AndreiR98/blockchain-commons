package uk.co.roteala.common.messenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.exceptions.SerializationException;
import uk.co.roteala.exceptions.errorcodes.SerializationErrorCode;
import uk.co.roteala.security.utils.HashingService;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class MessengerUtils {
    private final static Integer DEFAULT_CHUNK_SIZE = 1024;

    public final static String delimiter = "\n";

    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static Message deserialize(String messageWrapperString) {
        try {
            Message messageWrapper = objectMapper.readValue(messageWrapperString, Message.class);

            if (messageWrapper instanceof MessageKey) {
                return (MessageKey) messageWrapper;
            } else if (messageWrapper instanceof MessageChunk) {
                return (MessageChunk) messageWrapper;
            } else {
                return defaultMessage();
            }
        } catch (Exception e) {
            log.info("Error:{}", e);
            return null;
        }
    }

    private static Message defaultMessage() {
        log.info("Default message!");
        return new Message() {
        };
    }

    public List<String> createChunks(MessageTemplate template) {
        //template.setWithOut(new ArrayList<>());

        byte[] availableBytes = SerializationUtils.serialize(modelToString(template.getMessage()));
        final int totalMessageSize = availableBytes.length;

        List<String> messageChunks = new ArrayList<>();

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

                byte[] serializedChunk = SerializationUtils.serialize(serializeMessage(chunk));
                int size = serializedChunk.length;

                if (size >= DEFAULT_CHUNK_SIZE) {
                    break;
                }
            }

            messageChunks.add(serializeMessage(chunk));
            chunkNumber++;
        }

        key.setTotalChunks(chunkNumber);

        messageChunks.add(serializeMessage(key));

        return messageChunks;
    }

    private String formatToBytes(List<Byte> bytesList) {
        byte[] bytesData = new byte[bytesList.size()];

        for (int i = 0; i < bytesList.size(); i++) {
            bytesData[i] = bytesList.get(i);
        }

        return HashingService.bytesToHexString(bytesData);
    }

    public String serializeMessage(Message message) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.writeValueAsString(message);
        } catch (Exception e) {
            throw new SerializationException(SerializationErrorCode.SERIALIZATION_FAILED);
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
