package uk.co.roteala.common.events;

import lombok.*;
import uk.co.roteala.common.BaseModel;
import uk.co.roteala.net.Peer;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PeersContainer extends BaseModel {
    private List<Peer> peersList;
}
