package uk.co.roteala.net;

import lombok.*;
import uk.co.roteala.common.BaseEmptyModel;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Peer extends BaseEmptyModel {
    private String address;

    private boolean active;
    private long lastTimeSeen;
}
