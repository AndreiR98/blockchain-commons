package uk.co.roteala.common;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Chain implements Serializable {
    private String name;
    private String uniqueId;
    private ChainType chainType;
    private ChainStatus status;
    private BaseCoinModel coin;
    private List<Integer> blocks;
}
