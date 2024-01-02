package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.CoinConverter;
import uk.co.roteala.utils.Constants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("STATECHAIN")
public class ChainState extends BasicModel {
    private Integer lastBlockIndex;
    private BigInteger reward;
    private Integer target;
    private BigInteger networkFees;
    private Integer chainId;
    private Integer netVersion;
    private boolean allowEmptyMining;

    @Override
    public String serialize() {
        return super.serialize();
    }
}
