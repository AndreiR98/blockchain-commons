package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.CoinConverter;
import uk.co.roteala.security.PublicKey;
import uk.co.roteala.security.utils.CryptographyUtils;
import uk.co.roteala.security.utils.HashingService;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**Raw transaction data coming from wallet website
 * */
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("MEMPOOLTRANSACITON")
public class MempoolTransaction extends BasicModel {
    private String pseudoHash;
    private String from;
    private String to;
    private Integer version;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin value;
    private BigInteger fees;
    private Integer nonce;
    private long timeStamp;
    private TransactionStatus status;
    private String pubKeyHash;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SignatureModel signature;

    /**
     * Compute signing hash
     *
     * @return String
     * */
    @JsonIgnore
    public String computeSigningHash() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("from", this.from);
        map.put("to", this.to);
        map.put("version", this.version);
        map.put("value", "0x"+this.value.getStringValue());
        map.put("fees", "0x"+this.fees.toString(16));
        map.put("nonce", this.nonce);
        map.put("time_stamp", this.timeStamp);
        map.put("pub_key_hash", this.pubKeyHash);

        Map<String, Object> sortedMap = new TreeMap<>(map);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(sortedMap);

        return HashingService.bytesToHexString(HashingService.sha256Hash(jsonString.getBytes()));
    }

    /**
     * Recover the public key from the signature
     * Match the recovery key hash with the pukeyHash
     * Match the recovery key hash with the hash from address
     * */
    @JsonIgnore
    public boolean verifySignatureWithRecovery() {
        // Retrieve all possible keys for this signature
        try {
            List<PublicKey> publicKeys = CryptographyUtils.recoverPublicKeys(this);

            for (PublicKey publicKey : publicKeys) {
                if (CryptographyUtils.checkKeyWithPubKeyHash(publicKey, this.pubKeyHash)
                        && CryptographyUtils.checkKeyWithAddress(publicKey, this.from)) {
                    return true;
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
