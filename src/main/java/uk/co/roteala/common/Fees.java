package uk.co.roteala.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.CoinConverter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Fees implements Serializable {
    @JsonSerialize(converter = CoinConverter.class)
    private Coin fees;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin networkFees;


    public Map<String, String> format() {
        Map<String, String> map = new TreeMap<>();
        map.put("fees", String.valueOf(fees.getValue()));
        map.put("network_fees", String.valueOf(networkFees.getValue()));
        //map.put("v", String.valueOf(this.v));

        return map;
    }
}
