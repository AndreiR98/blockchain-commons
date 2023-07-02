package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.hash.Hashing;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.netty.tcp.TcpServer;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.CoinConverter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseModel {
    private String hash;
    private String pseudoHash;
    private String blockHash;
    private Integer blockNumber;
    private String from;
    private String to;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin fees;
    private Integer version;
    private Integer transactionIndex;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin value;
    private long timeStamp;
    private long confirmations;
    private long blockTime;
    private TransactionStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SignatureModel signature;

    public void setHash() throws JsonProcessingException {
        this.hash = Hashing.sha256().hashString(Hashing.sha256()
                        .hashString(prepareToHash(), StandardCharsets.UTF_8)
                        .toString(), StandardCharsets.UTF_8)
                .toString();
    }

    //For miner
    /**Very important that each miner will generate THE SAME hash for each transaciton in block, this way when block is created
     * and brodcasted each miner based on the hashed will generate take the transaciton from mempool
     * */
    private String prepareToHash() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        HashMap map = new HashMap<>();
        map.put("block_number", blockNumber);
        map.put("transactionIndex", transactionIndex);
        map.put("block_time", blockTime);
        map.put("fees", fees.getValue());
        map.put("amount", value.getValue());
        map.put("signature", signature);
        map.put("version", version);
        map.put("from_address", from);
        map.put("to_address", to);
        map.put("time_stamp", timeStamp);
        map.put("pseudoHash", pseudoHash);
        //map.put("transaction_index", transactionIndex);

        return mapper.writeValueAsString(map);
    }



    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
