package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.CoinConverter;
import uk.co.roteala.security.utils.HashingService;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;


@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("TRANSACTION")
public class Transaction extends BasicModel implements Serializable {
    private String hash;
    private String pseudoHash;
    private Integer blockNumber;
    private String from;
    private String to;
    private BigInteger fees;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin value;
    private Integer version;
    private Integer transactionIndex;
    private Integer nonce;
    private long timeStamp;
    private long confirmations;
    private long blockTime;
    private String pubKeyHash;
    private TransactionStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SignatureModel signature;

    @JsonIgnore
    public String computeHash() {
        Map<String, Object> map = new HashMap<>();
        map.put("pseudoHash", this.pseudoHash);
        map.put("blockNumber", this.blockNumber);
        map.put("from", this.from);
        map.put("to", this.to);
        map.put("fees", "0x"+this.fees.toString(16));
        map.put("value", "0x"+this.value.getStringValue());
        map.put("version", this.version);
        map.put("transactionIndex", this.transactionIndex);
        map.put("nonce", this.nonce);
        map.put("timeStamp", this.timeStamp);
        map.put("blockTime", this.blockTime);
        map.put("pubKeyHash", this.pubKeyHash);
        map.put("signature", this.signature.format());

        Map<String, Object> sortedMap = new TreeMap<>(map);

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonString = null;

        try {
            jsonString = objectMapper.writeValueAsString(sortedMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "0x"+HashingService.computeSHA3(jsonString);
    }
}
