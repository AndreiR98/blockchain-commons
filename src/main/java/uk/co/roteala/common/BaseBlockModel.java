package uk.co.roteala.common;

import lombok.*;
import org.springframework.util.SerializationUtils;
import uk.co.roteala.utils.GlacierUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseBlockModel extends BaseEmptyModel implements Hashing {
    //header that is going to be hashed
    private Integer version;
    private String markleRoot;
    private long timeStamp;
    private BigInteger nonce;
    private String previousHash;
    private Integer numberOfBits;
    //end of header

    private Integer index;
    private String hash;
    private String miner;
    private List<String> transactions;
    private Integer confirmations;
    private BlockStatus status;
    private BigInteger difficulty;

    @Override
    public String hashHeader() throws NoSuchAlgorithmException {
        List<Object> map = new ArrayList<>();
        map.add(version);
        map.add(markleRoot);
        map.add(timeStamp);
        map.add(nonce);
        map.add(previousHash);
        return GlacierUtils.bytesToHex(GlacierUtils.generateSHA256Digest(SerializationUtils.serialize(map)));
    }

    @Override
    public byte[] message() {
        return new byte[0];
    }
}
