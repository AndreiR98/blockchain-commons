package uk.co.roteala.net;

import lombok.*;
import uk.co.roteala.common.BasicModel;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Peer extends BasicModel {
    private String address;
    private Integer port;
    private long lastTimeSeen;
    private boolean active;
}
