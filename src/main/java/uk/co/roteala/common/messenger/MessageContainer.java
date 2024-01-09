package uk.co.roteala.common.messenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.exceptions.SerializationException;
import uk.co.roteala.exceptions.errorcodes.SerializationErrorCode;
import uk.co.roteala.security.utils.HashingService;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Slf4j
public class MessageContainer implements Serializable {
    private MessageKey key;
    private List<MessageChunk> chunks = new ArrayList<>();

    public boolean canAggregate() {
        if (key == null || key.getMessageId() == null || chunks.isEmpty() || chunks.size() != key.getTotalChunks()) {
            return false;
        }

        String messageId = key.getMessageId();
        int totalBytes = 0;

        for (MessageChunk chunk : chunks) {
            if (!messageId.equals(chunk.getMessageId())) {
                return false;
            }
            totalBytes += chunk.getChunkSize();
        }

        return totalBytes == key.getTotalBytes();
    }

    public MessageTemplate aggregate() {
        if (!canAggregate()) {
            throw new SerializationException(SerializationErrorCode.DESERIALIZATION_FAILED); // or throw an exception
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BasicModel basicModel = null;
        MessageTemplate messageTemplate = new MessageTemplate();

        chunks.forEach(chunk -> outputStream.writeBytes(HashingService.hexStringToByteArray(chunk.getHexedBytes())));

        byte[] aggregatedBytes = outputStream.toByteArray();

        try {
            String messageModelString = SerializationUtils.deserialize(aggregatedBytes);
            ObjectMapper objectMapper = new ObjectMapper();
            basicModel = objectMapper.readValue(messageModelString, BasicModel.class);

            messageTemplate.setMessage(basicModel);
            messageTemplate.setEventType(this.key.getEventType());
            messageTemplate.setEventAction(this.key.getEventAction());
            messageTemplate.setMessageId(this.key.getMessageId());
            messageTemplate.setHandler(this.key.getHandler());
            messageTemplate.setOwner(this.key.getOwner());
        } catch (Exception e) {
            log.error("Failed to aggregate message!", e);
        }

        return messageTemplate;
    }

    public void addChunk(MessageChunk chunk) {
        if (chunk != null && chunks.stream()
                .noneMatch(c -> Objects.equals(c.getChunkNumber(), chunk.getChunkNumber()))) {
            chunks.add(chunk);
        }
    }
}
