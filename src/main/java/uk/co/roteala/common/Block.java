package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import uk.co.roteala.common.storage.ColumnFamilyTypes;
import uk.co.roteala.common.storage.Storage;
import uk.co.roteala.exceptions.MiningException;
import uk.co.roteala.exceptions.SerializationException;
import uk.co.roteala.exceptions.errorcodes.MiningErrorCode;
import uk.co.roteala.exceptions.errorcodes.SerializationErrorCode;
import uk.co.roteala.security.utils.HashingService;
import uk.co.roteala.utils.BlockchainUtils;
import uk.co.roteala.utils.Constants;

import java.nio.charset.StandardCharsets;
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
        return this.header.getHash();
    }

    @JsonIgnore
    public Integer getIndex() {
        return this.header.getIndex();
    }

    @JsonIgnore
    public byte[] getKey() {
        return this.header.getKey();
    }

    @JsonIgnore
    public String computeHash() {
        Map<String, Object> map = new HashMap<>();
        map.put("version", this.header.getVersion());
        map.put("markleRoot", this.header.getMarkleRoot());
        map.put("timeStamp", this.header.getTimeStamp());
        map.put("nonce", this.header.getNonce());
        map.put("miner", this.header.getMinerAddress());
        map.put("index", this.header.getIndex());
        map.put("blockTime", this.header.getBlockTime());
        map.put("previousHash", this.header.getPreviousHash());
        map.put("difficulty", this.header.getDifficulty());
        map.put("numberOfTransactions", this.header.getNumberOfTransactions());

        Map<String, Object> sortedMap = new TreeMap<>(map);

        String jsonString = null;

        try {
            jsonString = super.mapper.writeValueAsString(sortedMap);
        }catch (Exception e) {
            throw new SerializationException(SerializationErrorCode.SERIALIZATION_FAILED);
        }

        return HashingService.bytesToHexString(HashingService.doubleSHA256(jsonString.getBytes()));
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

    @JsonIgnore
    public boolean isValidBlock(Storage transactionStorage, int target) {
        try {
            // Verify if the hash matches
            if (!this.getHash().equals(this.computeHash())) {
                return false; // or throw new MiningException(MiningErrorCode.HASH_MATCH);
            }

            // Check if the Markle root is correct
            if (!this.getTransactions().isEmpty() &&
                    !Objects.equals(this.getHeader().getMarkleRoot(),
                            BlockchainUtils.markleRootGenerator(this.getTransactions()))) {
                return false; // or throw new MiningException(MiningErrorCode.MARKLEROOT);
            }

            if (this.getTransactions().isEmpty() &&
                    !Objects.equals(this.getHeader().getMarkleRoot(), Constants.DEFAULT_HASH)) {
                return false; // or throw new MiningException(MiningErrorCode.MARKLEROOT);
            }

            // Check previous hash (consider adding this logic)

            // Check if the hash meets the target difficulty
            if (!BlockchainUtils.computedTargetValue(this.getHash(), target)) {
                return false; // or throw new MiningException(MiningErrorCode.TARGET);
            }

            // Verify each transaction hash
            for (String mempoolTransactionHash : this.getTransactions()) {
                if (!BlockchainUtils.validTransactionHash(mempoolTransactionHash) &&
                        !transactionStorage.has(ColumnFamilyTypes.TRANSACTIONS, mempoolTransactionHash
                                .getBytes(StandardCharsets.UTF_8))) {
                    return false; // or throw new MiningException(MiningErrorCode.TRANSACTION_NOT_FOUND);
                }
            }

            return true;
        } catch (MiningException e) {
            return false;
        }
    }


}
