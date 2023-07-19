package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.hash.Hashing;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.netty.tcp.TcpServer;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.CoinConverter;
import uk.co.roteala.security.utils.HashingService;

import java.nio.charset.StandardCharsets;
import java.util.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseModel {
    private String hash;
    private String pseudoHash;
    private Integer blockNumber;
    private String from;
    private String to;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin fees;
    private Integer version;
    private Integer transactionIndex;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin value;
    private Integer nonce;
    private long timeStamp;
    private long confirmations;
    private long blockTime;
    private String pubKeyHash;
    @JsonDeserialize(using = TransactionStatusDeserializer.class)
    @JsonSerialize(converter = TransactionStatusConverter.class)
    private TransactionStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SignatureModel signature;

    public String computeHash() {
        Map<String, Object> map = new HashMap<>();
        map.put("pseudoHash", this.pseudoHash);
        map.put("blockNumber", this.blockNumber);
        map.put("from", this.from);
        map.put("to", this.to);
        map.put("fees", String.valueOf(this.fees.getValue()));
        map.put("version", this.version);
        map.put("transactionIndex", this.transactionIndex);
        map.put("value", String.valueOf(this.value.getValue()));
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

        return HashingService.bytesToHexString(HashingService.sha256Hash(jsonString.getBytes()));
    }
}
