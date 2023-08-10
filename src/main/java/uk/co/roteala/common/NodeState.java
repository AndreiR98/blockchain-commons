package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("NODESTATE")
public class NodeState extends BaseModel {
    private Integer remainingBlocks;
    private Instant updatedAt;
}
