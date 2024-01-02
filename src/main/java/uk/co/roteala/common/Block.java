package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import uk.co.roteala.exceptions.SerializationException;
import uk.co.roteala.exceptions.errorcodes.SerializationErrorCode;

import java.util.*;


/**
 * Block structure, metadata + header is stored in storage whereas header is going to be broadcast
 * */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("BLOCK")
public class Block extends BasicModel {

    //Block header
    private BlockHeader header;

    //Meta data of block
    private List<String> transactions;
    private Integer numberOfBits;
    private String forkHash;
    private Integer confirmations;
    private BlockStatus status;

    @JsonIgnore
    public String serialize() {
        return super.serialize();
    }

    @JsonIgnore
    public String getHash() {
        final String hash = this.header.getHash();

        return (hash == null || hash.isEmpty()) ? "" : hash;
    }
    @JsonIgnore
    private String getTransactionAsString() {
        TreeSet<String> sortedMap = new TreeSet<>();

        this.getTransactions().forEach(sortedMap::add);

        String jsonString = null;

        try {
            jsonString = super.mapper.writeValueAsString(sortedMap);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

        return jsonString;
    }

}
