package uk.co.roteala.net;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import uk.co.roteala.common.BasicModel;

import java.nio.charset.StandardCharsets;

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

    @JsonIgnore
    public byte[] getKey() {
        StringBuilder s = new StringBuilder();
        s.append(this.address);
        s.append(this.port);

        return s.toString().getBytes(StandardCharsets.UTF_8);
    }
}
