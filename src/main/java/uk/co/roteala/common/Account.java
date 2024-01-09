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
import uk.co.roteala.common.monetary.VirtualBalanceSign;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
    private BigInteger nonce;
    private BigInteger balance;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigInteger virtualBalance;//keep account changes
    private List<String> transactionsIn;
    private List<String> transactionsOut;

    //@Override
    public String getAddress() {
        String hexString = this.address.startsWith("0x") ?
                this.address.substring(2) : this.address;

        // Convert to lowercase
        return "0x" + hexString.toLowerCase();
    }

    public void setAddress(String address) {
        String hexString = address.startsWith("0x") ?
                address.substring(2) : address;

        // Convert to lowercase
        this.address =  "0x" + hexString.toLowerCase();
    }

    @Override
    public String getHash() {
        String hexString = this.address.startsWith("0x") ?
                this.address.substring(2) : this.address;

        // Convert to lowercase
        return "0x" + hexString.toLowerCase();
    }

    @Override
    public String serialize() {
        return super.serialize();
    }

    @JsonIgnore
    public void updateVirtualBalance(BigInteger amount, VirtualBalanceSign sign) {
        BigInteger newValue;

        if (sign == VirtualBalanceSign.PLUS) {
            newValue = this.virtualBalance.add(amount);
        } else if (sign == VirtualBalanceSign.MINUS) {
            newValue = this.virtualBalance.subtract(amount);
        } else {
            throw new IllegalArgumentException("Invalid VirtualBalanceSign");
        }
        this.virtualBalance = newValue;
    }


    @JsonIgnore
    public String toWei() {
        return "0x"+this.balance.toString(16);
    }

    /**
     * Return an empty account with address
     * */
    @JsonIgnore
    public static final Account empty(String address) {
        return Account.builder()
                .address(address)
                .nonce(BigInteger.ZERO)
                .balance(BigInteger.ZERO)
                .virtualBalance(BigInteger.ZERO)
                .transactionsIn(new ArrayList<>())
                .transactionsOut(new ArrayList<>())
                .build();
    }
}
