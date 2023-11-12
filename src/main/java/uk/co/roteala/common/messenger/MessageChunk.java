package uk.co.roteala.common.messenger;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageChunk extends Message{
    private byte[] payload;
    private Integer chunkNumber;
    private Integer chunkSize;
}
