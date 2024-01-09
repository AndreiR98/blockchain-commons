package uk.co.roteala.common.events;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import uk.co.roteala.common.BasicModel;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("PEERSCONTAINER")
public class PeersContainer extends BasicModel implements Serializable {
    private List<String> peersList;

    @Override
    public String getHash() {
        return null;
    }
}
