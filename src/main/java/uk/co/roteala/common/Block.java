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


/**
 * Block structure, metadata + header is stored in storage whereas header is going to be broadcast
 * */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Block extends BaseModel {

    //Block header
    private BlockHeader header;

    //Meta data of block
    private List<String> transactions;
    private Integer numberOfBits;
    private String forkHash;
    private Integer confirmations;
    private BlockStatus status;

    public String getHash() {
        final String hash = this.header.getHash();

        return (hash == null || hash.isEmpty()) ? "" : hash;
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
