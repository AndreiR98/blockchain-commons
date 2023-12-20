package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.CoinConverter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

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
    private String balance;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String virtualBalance;//keep account changes
    private List<Coin> coins;

    @JsonIgnore
    public BigInteger toBigInt() {
        return new BigInteger(this.balance, 16);
    }

    @JsonIgnore
    public void updateBalance(String amount) {
        this.balance = new BigInteger(this.balance, 16)
                .add(new BigInteger(amount, 16)).toString(16);
    }

    @JsonIgnore
    public void updateBalance(BigInteger amount) {
        this.balance = new BigInteger(this.balance, 16)
                .add(amount).toString(16);
    }

    /**
     * Return an empty account with address
     * */
    @JsonIgnore
    public static final Account empty(String address) {
        return Account.builder()
                .address(address)
                .nonce("0x0")
                .balance("0x0")
                .build();
    }
}
