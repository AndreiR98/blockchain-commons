package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.monetary.CoinConverter;
import uk.co.roteala.common.monetary.CoinDeserializer;
import uk.co.roteala.security.ECDSA;
import uk.co.roteala.security.ECKey;
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
public class PseudoTransaction extends BaseModel {
    private String pseudoHash;
    private String from;
    private String to;
    private Integer version;
    @JsonSerialize(converter = CoinConverter.class)
    private Coin value;
    private Integer nonce;
    private long timeStamp;
    @JsonDeserialize(using = TransactionStatusDeserializer.class)
    @JsonSerialize(converter = TransactionStatusConverter.class)
    private TransactionStatus status;
    private String pubKeyHash;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SignatureModel signature;

    /**
     * Compute the hash of the pseudo transaction
     * */
    public String computeHash() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("from", this.from);
        map.put("to", this.to);
        map.put("version", this.version);
        map.put("value", String.valueOf(this.value.getValue()));
        map.put("nonce", this.nonce);
        map.put("timeStamp", this.timeStamp);
        map.put("pubKeyHash", this.pubKeyHash);
        map.put("signature", this.signature.format());

        Map<String, Object> sortedMap = new TreeMap<>(map);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(sortedMap);

        return HashingService.bytesToHexString(HashingService.sha256Hash(jsonString.getBytes()));
    }

    /**
     * Compute signing hash
     *
     * @return String
     * */
    public String computeSigningHash() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("from", this.from);
        map.put("to", this.to);
        map.put("version", this.version);
        map.put("value", String.valueOf(this.value.getValue()));
        map.put("nonce", this.nonce);
        map.put("timeStamp", this.timeStamp);
        map.put("pubKeyHash", this.pubKeyHash);

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
    public boolean verifySignatureWithRecovery() {
        // Retrieve all possible keys for this signature
        try {
            List<PublicKey> publicKeys = CryptographyUtils.recoverPublicKeys(this);

            for (PublicKey publicKey : publicKeys) {
                CryptographyUtils.checkKeyWithPubKeyHash(publicKey, this.pubKeyHash);
                CryptographyUtils.checkKeyWithAddress(publicKey, this.from);
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
