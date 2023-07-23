package uk.co.roteala.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.CoinConverter;
import uk.co.roteala.security.utils.HashingService;

import java.math.BigInteger;
import java.util.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Block extends BaseModel {
    //header that is going to be hashed
    private Integer version;
    private String markleRoot;
    private long timeStamp;
    private String nonce;
    private String previousHash;
    private Integer numberOfBits;
    private Integer difficulty;
    private List<String> transactions;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin reward;
    private String miner;
    private Integer index;
    //end of header

    private String hash;
    private String forkHash;
    private Integer confirmations;
    private BlockStatus status;

    public String computeHash() {
        Map<String, Object> map = new HashMap<>();
        map.put("version", this.version);
        map.put("markleRoot", this.markleRoot);
        map.put("timeStamp", this.timeStamp);
        map.put("reward", String.valueOf(this.reward.getValue()));
        map.put("nonce", this.nonce);
        map.put("miner", this.miner);
        map.put("index", this.index);
        map.put("previousHash", this.previousHash);
        map.put("numberOfBits", this.numberOfBits);
        map.put("difficulty", this.difficulty);
        map.put("transactions", this.getTransactionAsString());

        Map<String, Object> sortedMap = new TreeMap<>(map);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;

        try {
            jsonString = objectMapper.writeValueAsString(sortedMap);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

        return HashingService.bytesToHexString(HashingService.sha256Hash(jsonString.getBytes()));
    }

    private String getTransactionAsString() {
        TreeSet<String> sortedMap = new TreeSet<>();

        this.getTransactions().forEach(sortedMap::add);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;

        try {
            jsonString = objectMapper.writeValueAsString(sortedMap);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

        return jsonString;
    }


}
