package uk.co.roteala.common;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NodeState extends BaseModel {
    private Integer remainingBlocks;
    private Instant updatedAt;
}
