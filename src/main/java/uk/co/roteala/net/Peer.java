package uk.co.roteala.net;

import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Peer implements Serializable {
    private String address;

    private boolean active;
    private long lastTimeSeen;
}
