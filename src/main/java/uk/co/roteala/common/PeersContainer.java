package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.vertx.core.net.NetSocket;
import lombok.*;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.net.Peer;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("PEERSCONTAINER")
public class PeersContainer extends BasicModel implements Serializable {
    private List<Peer> peersList;

    @Override
    public byte[] getKey() {
        return null;
    }
}
