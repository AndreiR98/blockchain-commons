package uk.co.roteala.common.messenger;

import lombok.Data;
import uk.co.roteala.common.BasicModel;

import java.io.Serializable;
import java.util.List;

@Data
public class MessageContainer implements Serializable {
    private MessageKey key;
    private List<MessageChunk> chunks;

    public boolean canAggregate() {
        if (key == null || key.getMessageId() == null || chunks.isEmpty()) {
            return false;
        }

        if (chunks.size() != key.getTotalChunks()) {
            return false;
        }

        if (chunks.stream()
                .anyMatch(chunk -> !chunk.getMessageId()
                        .equals(key.getMessageId()))) {
            return false;
        }

        int totalBytes = chunks.stream()
                .mapToInt(MessageChunk::getChunkSize)
                .sum();

        return totalBytes == key.getTotalBytes();
    }

    //return the aggregated value of the message
    public BasicModel aggregate() {
        byte[] messageBytes;

        //aggregate bytes into message template same as events
    }
}
