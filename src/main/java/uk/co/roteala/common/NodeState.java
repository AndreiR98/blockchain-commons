package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("NODESTATE")
public class NodeState extends BaseModel implements Serializable {
    private Integer remainingBlocks;
    private Integer lastBlockIndex;
    private long updatedAt;
}
