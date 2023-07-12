package uk.co.roteala.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.CoinConverter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountModel extends BaseModel implements Serializable {
    private String address;
    private Integer nonce;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin balance;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin inboundAmount;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin outboundAmount;

    /**
     * Return an empty account with address
     * */
    public AccountModel empty(String address) {
        return AccountModel.builder()
                .address(address)
                .nonce(0)
                .balance(Coin.ZERO)
                .inboundAmount(Coin.ZERO)
                .outboundAmount(Coin.ZERO)
                .build();
    }
}
