package uk.co.roteala.net;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Peer extends BaseEmptyModel {
    private String address;
    private Integer port;
    private long lastTimeSeen;
    private boolean active;
}
