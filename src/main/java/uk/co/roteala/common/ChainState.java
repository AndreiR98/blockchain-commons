package uk.co.roteala.common;

import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ChainState extends BaseModel {
    private Integer lastBlockIndex;
    private String minerKey;
    private BigInteger target;
    private List<String> accounts;
}
