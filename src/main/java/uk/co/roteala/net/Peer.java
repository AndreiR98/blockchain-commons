package uk.co.roteala.net;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.common.MempoolTransaction;

import java.nio.charset.StandardCharsets;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName(Peer.TYPE)
public class Peer extends BasicModel {
    static final String TYPE = "PEER";
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

    @JsonIgnore
    @Override
    public String getHash() {
        StringBuilder s = new StringBuilder();
        s.append(this.address);
        s.append(this.port);

        return s.toString();
    }
}
