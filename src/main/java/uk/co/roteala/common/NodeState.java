package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import uk.co.roteala.utils.Constants;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("NODESTATE")
public class NodeState extends BasicModel implements Serializable {
    private Integer remainingBlocks;
    private Integer lastBlockIndex;
    private long updatedAt;

    @Override
    public String getHash() {
        return Constants.DEFAULT_NODE_STATE;
    }

    @Override
    public String serialize() {
        return super.serialize();
    }
}
