package uk.co.roteala.common.messenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.common.events.MessageTemplate;
import uk.co.roteala.common.events.MessageWrapper;
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

        log.info("Total Key:{}, Total:{}", key.getTotalBytes(), totalBytes);
        return totalBytes == key.getTotalBytes();
    }

    public MessageTemplate aggregate() {
        if (!canAggregate()) {
            log.error("Cannot aggregate message!");
            return new MessageTemplate(); // or throw an exception
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BasicModel basicModel = null;
        MessageTemplate messageTemplate = new MessageTemplate();

        chunks.forEach(chunk -> outputStream.writeBytes(HashingService.hexStringToByteArray(chunk.getHexedBytes())));

        byte[] aggregatedBytes = outputStream.toByteArray();

        try {
            log.info("Start aggregating!");
            String messageModelString = SerializationUtils.deserialize(aggregatedBytes);
            ObjectMapper objectMapper = new ObjectMapper();
            basicModel = objectMapper.readValue(messageModelString, BasicModel.class);

            messageTemplate.setMessage(basicModel);
            messageTemplate.setType(this.key.getMessageType());
            messageTemplate.setActions(this.key.getAction());
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
