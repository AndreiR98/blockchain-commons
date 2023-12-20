package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import uk.co.roteala.security.PublicKey;
import uk.co.roteala.security.utils.CryptographyUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class RawTransaction implements Serializable {
    private BigInteger nonce;
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private String to;
    private String data;
    private String from;
    private BigInteger value;
    private Signature signature;

    //@Override
    @JsonIgnore
    public String getFrom() {
        //Compute from using signature
        final String from = computeFrom();

        if(this.from != null && Objects.equals(from, this.from)) {
            return this.from;
        } else {
            return from;
        }
    }

    /**
     * This method returns if the signed raw data is valid, it retrieves first the public key then
     * */
    @JsonIgnore
    public boolean isValid() {
        return true;
    }

    @JsonIgnore
    private String computeFrom() {
        return null;
    }

    /**
     * Recover the public key from the signature
     * Match the recovery key hash with the pukeyHash
     * Match the recovery key hash with the hash from address
     * */
    @JsonIgnore
    public boolean verifySignatureWithRecovery() {
        // Retrieve all possible keys for this signature
        //log.info("Number:{}", CryptographyUtils.recoverPublicKeyFromSignature(this));
//            List<PublicKey> publicKeys = CryptographyUtils.recoverPublicKeys(this);
//
//            for (PublicKey publicKey : publicKeys) {
//                log.info("Key:{}", publicKey.toAddress());
////                if (CryptographyUtils.checkKeyWithPubKeyHash(publicKey, this.pubKeyHash)
////                        && CryptographyUtils.checkKeyWithAddress(publicKey, this.from)) {
////                    return true;
////                }
//            }

        return false;
    }
}
