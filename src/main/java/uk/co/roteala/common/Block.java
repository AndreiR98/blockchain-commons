package uk.co.roteala.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import lombok.*;
import org.springframework.util.SerializationUtils;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.utils.GlacierUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private BigInteger nonce;
    private String previousHash;
    private Integer numberOfBits;
    private BigInteger difficulty;
    private List<String> transactions;
    private Coin reward;
    private String miner;
    private Integer index;
    //end of header

    private String hash;
    private String forkHash;
    private Integer confirmations;
    private BlockStatus status;

    public void setHash() throws JsonProcessingException {
        this.hash = com.google.common.hash.Hashing.sha256().hashString(Hashing.sha256()
                        .hashString(prepareToHash(), StandardCharsets.UTF_8)
                        .toString(), StandardCharsets.UTF_8)
                .toString();
    }

    //For miner
    private String prepareToHash() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        HashMap map = new HashMap<>();
        map.put("markle_root", markleRoot);
        map.put("reward", reward);
        map.put("transactions", transactions);
        map.put("nonce", nonce);
        map.put("previous_hash", previousHash);
        map.put("number_of_bits", numberOfBits);
        map.put("difficulty", difficulty);
        map.put("time_stamp", timeStamp);
        map.put("miner", miner);
        map.put("index", index);
        map.put("version", version);

        return mapper.writeValueAsString(map);
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
