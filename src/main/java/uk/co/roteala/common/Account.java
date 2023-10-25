package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.CoinConverter;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("ACCOUNT")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Account extends BasicModel implements Serializable {
    private String address;
    private String nonce;
    private BigInteger balance;

    /**
     * Return an empty account with address
     * */
    @JsonIgnore
    public Account empty(String address) {
        return Account.builder()
                .address(address)
                .nonce("0")
                .balance(new BigInteger("0", 16))
                .build();
    }
}
