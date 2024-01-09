package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import uk.co.roteala.exceptions.SerializationException;
import uk.co.roteala.exceptions.errorcodes.SerializationErrorCode;
import uk.co.roteala.security.PublicKey;
import uk.co.roteala.security.utils.CryptographyUtils;
import uk.co.roteala.security.utils.HashingService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName(MempoolTransaction.TRANSACTION_TYPE)
public class MempoolTransaction extends BasicModel {
    static final String TRANSACTION_TYPE = "MEMPOOLTRANSACTION";

    private String hash;
    private String from;
    private String to;
    private Integer version;
    private BigInteger amount;
    private String nonce;
    private long timeStamp;
    private BigInteger networkFees;
    private BigInteger fees;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TransactionStatus status;
    private String pubKeyHash;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SignatureModel signature;

    @JsonIgnore
    private Map<String, Object> createTransactionMap(boolean includeHash) {
        Map<String, Object> map = new TreeMap<>();
        map.put("type", TRANSACTION_TYPE);
        map.put("from", this.from);
        map.put("to", this.to);
        map.put("version", this.version);
        map.put("amount", this.amount.toString());
        map.put("nonce", this.nonce);
        map.put("timeStamp", this.timeStamp);
        map.put("networkFees", this.networkFees.toString());
        map.put("fees", this.fees.toString());
        map.put("pubKeyHash", this.pubKeyHash);

        if (includeHash) {
            map.put("hash", this.hash);
        }
        return map;
    }

    @Override
    public String serialize() {
        return super.serialize();
    }

    @JsonIgnore
    public byte[] getKey() {
        return this.hash.getBytes(StandardCharsets.UTF_8);
    }

    @JsonIgnore
    public static MempoolTransaction create(String rawData) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            MempoolTransaction mempoolTransaction = mapper.readValue(rawData, MempoolTransaction.class);
            mempoolTransaction.setStatus(TransactionStatus.PENDING);
            return mempoolTransaction;
        } catch (Exception e) {
            throw new SerializationException(SerializationErrorCode.DESERIALIZATION_FAILED);
        }
    }

    @JsonIgnore
    public String computeHash(boolean mode) {
        try {
            ObjectMapper objectMapper = super.mapper;
            String jsonString = objectMapper.writeValueAsString(createTransactionMap(mode));
            return "0x" + HashingService.bytesToHexString(HashingService.sha256Hash(jsonString.getBytes()));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @JsonIgnore
    public String computeSigningHash() {
        try {
            ObjectMapper objectMapper = super.mapper;
            String jsonString = objectMapper.writeValueAsString(createTransactionMap(true));
            return HashingService.bytesToHexString(HashingService.computeKeccak(jsonString.getBytes()));
        } catch (Exception e) {
            throw new SerializationException(SerializationErrorCode.SERIALIZATION_FAILED);
        }
    }

    @JsonIgnore
    public String computeJSONstring() {
        try {
            ObjectMapper objectMapper = super.mapper;
            String jsonString = objectMapper.writeValueAsString(createTransactionMap(true));
            return jsonString;
        } catch (Exception e) {
            throw new SerializationException(SerializationErrorCode.SERIALIZATION_FAILED);
        }
    }

    @JsonIgnore
    public boolean verifyTransaction() {
        if (!Objects.equals(computeHash(false), this.hash)) {
            return false;
        }

        if (!CryptographyUtils.isValidAddress(this.from) || !CryptographyUtils.isValidAddress(this.to)) {
            return false;
        }

        List<PublicKey> publicKeys;
        try {
            publicKeys = CryptographyUtils.recoverPublicKeys(this);
        } catch (Exception e) {
            return false;
        }

        for (PublicKey publicKey : publicKeys) {
            boolean isKeyMatching = CryptographyUtils.checkKeyWithPubKeyHash(publicKey, this.pubKeyHash);
            boolean isFromAddressMatching = CryptographyUtils.checkKeyWithAddress(publicKey, this.from);

            if (isKeyMatching && isFromAddressMatching) {
                return true;
            }
        }

        return false;
    }
}
