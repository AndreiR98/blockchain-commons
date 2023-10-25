package uk.co.roteala.common.messenger;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private String messageId;
    private MessageType messageType;
    private byte[] payload;
    private Integer chunkNumber;
    private Integer sequenceChunkNumber;
    private String checkSum;
}
