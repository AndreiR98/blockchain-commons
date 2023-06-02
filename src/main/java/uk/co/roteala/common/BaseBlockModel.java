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
    private BigInteger difficulty;
    private List<String> transactions;
    //end of header

    private Integer index;
    private String hash;
    private String miner;

    private Integer confirmations;
    private BlockStatus status;


    @Override
    public String hashHeader() throws NoSuchAlgorithmException {
        List<Object> map = new ArrayList<>();
        map.add(version);
        map.add(markleRoot);
        map.add(timeStamp);
        map.add(transactions);
        map.add(nonce);
        map.add(previousHash);
        map.add(numberOfBits);
        map.add(difficulty);
        return GlacierUtils.bytesToHex(GlacierUtils.generateSHA256Digest(GlacierUtils.generateSHA256Digest(SerializationUtils.serialize(map))));
    }

    @Override
    public byte[] serialized() {
        List<Object> map = new ArrayList<>();
        map.add(version);
        map.add(markleRoot);
        map.add(timeStamp);
        map.add(nonce);
        map.add(previousHash);
        map.add(numberOfBits);
        map.add(difficulty);
        map.add(transactions);
        //end of header

        map.add(index);
        map.add(hash);
        map.add(miner);

        map.add(confirmations);
        map.add(status);

        return SerializationUtils.serialize(map);
    }
}
