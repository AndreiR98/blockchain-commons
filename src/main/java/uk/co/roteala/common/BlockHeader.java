package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.CoinConverter;
import uk.co.roteala.exceptions.SerializationException;
import uk.co.roteala.exceptions.errorcodes.SerializationErrorCode;
import uk.co.roteala.security.utils.HashingService;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("BLOCKHEADER")
public class BlockHeader extends BasicModel{
    private String previousHash;
    private String hash;
    private String markleRoot;
    private long timeStamp;
    private long blockTime;
    private String nonce;
    private Integer numberOfTransactions;
    private String minerAddress;
    private Integer version;
    private Integer index;
    private Integer difficulty;

    @Override
    public String serialize() {
        return super.serialize();
    }

    @Override
    public byte[] getKey() {
        return this.index.toString().getBytes(StandardCharsets.UTF_8);
    }

    @JsonIgnore
    public void setHash() {
        Map<String, Object> map = new HashMap<>();
        map.put("version", this.version);
        map.put("markleRoot", this.markleRoot);
        map.put("timeStamp", this.timeStamp);
        map.put("nonce", this.nonce);
        map.put("miner", this.minerAddress);
        map.put("index", this.index);
        map.put("blockTime", this.blockTime);
        map.put("previousHash", this.previousHash);
        map.put("difficulty", this.difficulty);
        map.put("numberOfTransactions", this.numberOfTransactions);

        Map<String, Object> sortedMap = new TreeMap<>(map);

        String jsonString = null;

        try {
            jsonString = super.mapper.writeValueAsString(sortedMap);
        }catch (Exception e) {
            throw new SerializationException(SerializationErrorCode.SERIALIZATION_FAILED);
        }

        this.hash = HashingService.bytesToHexString(HashingService.doubleSHA256(jsonString.getBytes()));
    }
}
