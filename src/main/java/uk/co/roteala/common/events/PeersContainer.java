package uk.co.roteala.common.events;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import uk.co.roteala.common.BaseModel;
import uk.co.roteala.net.Peer;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("PEERSCONTAINER")
public class PeersContainer extends BaseModel implements Serializable {
    private List<String> peersList;
}
