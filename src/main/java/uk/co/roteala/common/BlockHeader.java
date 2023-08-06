package uk.co.roteala.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.security.utils.HashingService;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * BlockHeader represend the data that is broadcasted to other nodes and broker
 * This data is the mined data and based on this real blocks are constructed and transaction enriched
 * */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BlockHeader extends BaseModel {
    private String previousHash;
    private String hash;
    private String markleRoot;
    private long timeStamp;
    private String nonce;
    private Integer numberOfTransactions;
    private String minerAddress;
    private Integer version;
    private Integer index;
    private Integer difficulty;
    private Coin reward;

    public void setHash() {
        Map<String, Object> map = new HashMap<>();
        map.put("version", this.version);
        map.put("markleRoot", this.markleRoot);
        map.put("timeStamp", this.timeStamp);
        map.put("nonce", this.nonce);
        map.put("miner", this.minerAddress);
        map.put("index", this.index);
        map.put("reward", String.valueOf(this.reward.getValue()));
        map.put("previousHash", this.previousHash);
        map.put("difficulty", this.difficulty);
        map.put("numberOfTransactions", this.numberOfTransactions);

        Map<String, Object> sortedMap = new TreeMap<>(map);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;

        try {
            jsonString = objectMapper.writeValueAsString(sortedMap);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.hash = HashingService.bytesToHexString(HashingService.doubleSHA256(jsonString.getBytes()));
    }
}
