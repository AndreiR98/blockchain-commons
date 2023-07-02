package uk.co.roteala.net;

import lombok.*;
import uk.co.roteala.common.BaseModel;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Peer extends BaseModel {
    private String address;
    private Integer port;
    private long lastTimeSeen;
    private boolean active;
}
